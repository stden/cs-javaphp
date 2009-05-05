package ru.ipo.dces.debug;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.utils.ZipUtils;
import ru.ipo.dces.utils.PluginUtils;
import ru.ipo.dces.log.LoggerFactory;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.pluginapi.Plugin;

import java.util.HashMap;
import java.util.Random;
import java.util.Date;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 29.04.2009
 * Time: 14:45:05
 */
public class ServerPluginProxy implements ServerPluginEmulator {

  private ServerFacade server;
  private String sessionID;
  private int contestID;
  private int problemID;
  private String login;
  private String password;
  private boolean isSuperAdmin;
  private boolean selfRegistration;
  private File statementFolder;

  private String randomDebugString(int len) {
    Random r = new SecureRandom();
    StringBuilder b = new StringBuilder("_debug_");
    for (int i = 0; i < len; i++)
      b.append((char) ('a' + r.nextInt(26)));
    return b.toString();
  }

  private void connectToContest(int contestID) throws GeneralRequestFailureException, ServerReturnedError {
    ConnectToContestRequest request = new ConnectToContestRequest();
    ConnectToContestResponse response;

    String connectLogin;
    String connectPassword;

    if (!selfRegistration) {
      connectLogin = login;
      connectPassword = password;
    } else {
      UserDescription newUser = registerParticipant();

      connectLogin = newUser.login;
      connectPassword = newUser.password;
    }

    //login request
    request.contestID = contestID;
    request.login = connectLogin;
    request.password = connectPassword;
    response = server.doRequest(request);

    sessionID = response.sessionID;
  }

  private UserDescription registerParticipant() throws ServerReturnedError, GeneralRequestFailureException {
    //first get contest description
    GetContestDataRequest contestDataRequest = new GetContestDataRequest();
    contestDataRequest.sessionID = sessionID;
    contestDataRequest.contestID = contestID;
    GetContestDataResponse contestDataResponse =
            server.doRequest(contestDataRequest);

    UserDescription newUser = new UserDescription();
    //fill user
    newUser.login = randomDebugString(10);
    newUser.password = randomDebugString(10);
    this.login = newUser.login;
    this.password = newUser.password;
    newUser.dataValue = new String[contestDataResponse.contest.data.length];
    for (int i = 0; i < newUser.dataValue.length; i++)
      newUser.dataValue[i] = randomDebugString(5);

    //register user to contest
    RegisterToContestRequest registerToContestRequest =
            new RegisterToContestRequest();
    registerToContestRequest.contestID = contestID;
    registerToContestRequest.sessionID = sessionID;
    registerToContestRequest.user = newUser;

    server.doRequest(registerToContestRequest);    

    return newUser;
  }

  private void selectStatementFolder() {
    statementFolder = new File(System.getProperty("java.io.tmpdir") + randomDebugString(10));
  }

  public ServerPluginProxy(ServerFacade server) {
    this.server = server;
    isSuperAdmin = false;
    selfRegistration = true;
    selectStatementFolder();
  }

  public ServerPluginProxy(ServerFacade server, String login, String password, boolean isSuperAdmin) {
    this.server = server;
    this.login = login;
    this.password = password;
    this.isSuperAdmin = isSuperAdmin;
    selfRegistration = false;
    selectStatementFolder();
  }

  public void selectContest(int contestID) throws GeneralRequestFailureException, ServerReturnedError {
    if (!isSuperAdmin)
      connectToContest(contestID);
    else
      connectToContest(0);
    this.contestID = contestID;
  }

  public void createContest(ContestDescription contest) throws GeneralRequestFailureException, ServerReturnedError {
    connectToContest(0);

    CreateContestRequest createRequest = new CreateContestRequest();
    createRequest.sessionID = sessionID;
    createRequest.contest = contest;

    CreateContestResponse createResponse = server.doRequest(createRequest);
    contestID = createResponse.createdContestID;
  }

  public void createContest() throws ServerReturnedError, GeneralRequestFailureException {
    ContestDescription contestDescription = new ContestDescription();

    //create contest with default settings
    contestDescription.data = new UserDataField[0];
    contestDescription.description = randomDebugString(100);
    contestDescription.registrationType = ContestDescription.RegistrationType.ByAdmins;
    contestDescription.start = new Date();
    contestDescription.finish = new Date(System.currentTimeMillis() + 1000 * 60 * 60); //one hour
    contestDescription.name = randomDebugString(10);

    contestDescription.contestTiming = new ContestTiming();

    contestDescription.resultsAccessPolicy = new ResultsAccessPolicy();

    createContest(contestDescription);
  }

  public void adjustContest(ContestDescription contest) throws GeneralRequestFailureException, ServerReturnedError {
    AdjustContestRequest adjustRequest = new AdjustContestRequest();
    contest.contestID = contestID;
    adjustRequest.contest = contest;
    adjustRequest.problems = null;
    adjustRequest.sessionID = sessionID;
    server.doRequest(adjustRequest);
  }

  public void uploadServerPlugin(String alias, File phpPluginFile) throws IOException, GeneralRequestFailureException, ServerReturnedError {
    //fill request
    AdjustServerPluginRequest aspr = new AdjustServerPluginRequest();
    aspr.description = null;
    aspr.pluginAlias = alias;
    aspr.sessionID = sessionID;
    //load plugin data from file
    aspr.pluginData = new byte[(int)phpPluginFile.length()];
    FileInputStream in = new FileInputStream(phpPluginFile);
    int read = in.read(aspr.pluginData);
    if (read != aspr.pluginData.length)
      throw new IOException("Failed to read php file with plugin");

    //do request
    server.doRequest(aspr);
  }

  public void selectProblem(int problemID) {
    this.problemID = problemID;
  }

  public void createProblem(String clientPlugin, String serverPlugin, File statement, File answer)
          throws GeneralRequestFailureException, ServerReturnedError, IOException {
    //get contest data
    GetContestDataRequest gcdr = new GetContestDataRequest();
    gcdr.contestID = contestID;
    gcdr.extendedData = null;
    gcdr.infoType = GetContestDataRequest.InformationType.NoInfo;
    gcdr.sessionID = sessionID;

    GetContestDataResponse gcdResponse = server.doRequest(gcdr);

    //set new contest data
    AdjustContestRequest acr = new AdjustContestRequest();
    acr.contest = new ContestDescription();
    acr.contest.contestID = contestID;
    acr.sessionID = sessionID;
    //copy old problems ids
    acr.problems = new ProblemDescription[gcdResponse.problems.length + 1];
    for (int i = 0; i < gcdResponse.problems.length; i++) {
      ProblemDescription problem = gcdResponse.problems[i];
      acr.problems[i] = new ProblemDescription();
      acr.problems[i].id = problem.id;
    }
    ProblemDescription newProblem = new ProblemDescription();
    acr.problems[acr.problems.length - 1] = newProblem;

    newProblem.id = -1;
    newProblem.name = randomDebugString(10);
    newProblem.statementData = ZipUtils.zip(statement);
    newProblem.answerData = ZipUtils.zip(answer);
    newProblem.clientPluginAlias = clientPlugin;
    newProblem.serverPluginAlias = serverPlugin;

    AdjustContestResponse response = server.doRequest(acr);
    //problemID <- id of the last problem
    problemID = response.problemIDs[response.problemIDs.length - 1];
  }

  public void selectParticipant(String login, String password) throws GeneralRequestFailureException, ServerReturnedError {
    DisconnectRequest dr = new DisconnectRequest();
    dr.sessionID = sessionID;
    server.doRequest(dr);

    isSuperAdmin = false;
    this.login = login;
    this.password = password;
    connectToContest(contestID);
  }

  public void newParticipant() throws GeneralRequestFailureException, ServerReturnedError {
    //create a participant
    registerParticipant();

    selectParticipant(login, password);
  }

  public void setStatementFolder(File statementFolder) {
    this.statementFolder = statementFolder;
  }

  public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) throws GeneralRequestFailureException {
    final SubmitSolutionRequest solutionRequest = new SubmitSolutionRequest();
    solutionRequest.problemID = problemID;
    solutionRequest.problemResult = solution;
    solutionRequest.sessionID = sessionID;
    SubmitSolutionResponse r;

    try {
      r = server.doRequest(solutionRequest);
    } catch (ServerReturnedError serverReturnedError) {
      LoggerFactory.getLogger().log(serverReturnedError.getMessage(), LogMessageType.Error, null);
      throw new GeneralRequestFailureException();
    }

    for (Map.Entry<String, String> k2v : r.problemResult.entrySet())
      result.put(k2v.getKey(), k2v.getValue());
    return null; //don't using state
  }

  public Class<? extends Plugin> getClientPlugin(String alias) throws GeneralRequestFailureException, ServerReturnedError {
    return PluginUtils.forceGetPluginClass(server, alias, null);
  }

  public File getStatement() throws GeneralRequestFailureException, IOException {
    GetContestDataRequest gcdr = new GetContestDataRequest();
    gcdr.contestID = contestID;
    gcdr.extendedData = new int[]{problemID};
    gcdr.infoType = GetContestDataRequest.InformationType.ParticipantInfo;
    gcdr.sessionID = sessionID;

    GetContestDataResponse response;
    try {
      response = server.doRequest(gcdr);
    } catch (ServerReturnedError serverReturnedError) {
      LoggerFactory.getLogger().log(serverReturnedError.getMessage(), LogMessageType.Error, null);
      throw new GeneralRequestFailureException();
    }

    //find problem with the desired id
    byte[] statement = null;
    for (ProblemDescription problem : response.problems)
      if (problem.id == problemID) {
        statement = problem.statement;
        break;
      }

    if (statement == null) {
      LoggerFactory.getLogger().log("failed to get problem statement", LogMessageType.Error, null);
      throw new GeneralRequestFailureException();
    }

    ZipUtils.unzip(statement, statementFolder);

    statementFolder.mkdirs();
    return statementFolder;
  }
}