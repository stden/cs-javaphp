package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * ќписание контеста
 */
public class ContestDescription {

  public static enum RegistrationType {
    /** можно регистрироватьс€ самому с помощью  лиента */
    Self,
    /** регистриуют только администраторы */
    ByAdmins,    
  }

  /** ID контеста */
  public int              contestID;

  /** название контеста */
  public String           name;

  /** описание контеста */
  public String           description;

  /** врем€ начала контеста */
  public Date             start;

  /** врем€ окончани€ контеста */
  public Date             finish;

  /** способ регистрации на контест */
  public RegistrationType registrationType;

  /**
   * Ќабор данных об участнике, которые необходимо отображать
   */
  public UserDataField[]  data;

  //temporary, instead of userdata
//  public String[] data;
//  public boolean[] compulsory;

  /**
   * Ќастройка метода доступа к результатам
   */
  public ResultsAccessPolicy resultsAccessPolicy;

  /**
   * управление временем доступа
   */
  public ContestTiming contestTiming;

}
