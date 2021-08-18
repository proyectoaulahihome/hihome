/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     18/8/2021 0:36:52                            */
/*==============================================================*/


drop index RTBLDIVICE_RTBLDATA_FK;

drop index TBLDATA_PK;

drop table TBLDATA;

drop index RTBLUSER_RTBLDEVICE_FK;

drop index TBLDEVICE_PK;

drop table TBLDEVICE;

drop index RTBLDATA_TBLNOTI_FK;

drop index TBLNOTIFICATIONS_PK;

drop table TBLNOTIFICATIONS;

drop index TBLUSER_PK;

drop table TBLUSER;

/*==============================================================*/
/* Table: TBLDATA                                               */
/*==============================================================*/
create table TBLDATA (
   DATA_ID              SERIAL               not null,
   DEVICE_ID            INT4                 not null,
   MQGAS                INT4                 not null,
   MLX                  INT4                 not null,
   MQHUMO               INT4                 not null,
   U_DETECTED           VARCHAR(50)          not null,
   DATEUP               DATE                 not null,
   constraint PK_TBLDATA primary key (DATA_ID)
);

/*==============================================================*/
/* Index: TBLDATA_PK                                            */
/*==============================================================*/
create unique index TBLDATA_PK on TBLDATA (
DATA_ID
);

/*==============================================================*/
/* Index: RTBLDIVICE_RTBLDATA_FK                                */
/*==============================================================*/
create  index RTBLDIVICE_RTBLDATA_FK on TBLDATA (
DEVICE_ID
);

/*==============================================================*/
/* Table: TBLDEVICE                                             */
/*==============================================================*/
create table TBLDEVICE (
   DEVICE_ID            SERIAL               not null,
   USER_ID              INT4                 not null,
   NAMEDEVICE           VARCHAR(100)         not null,
   MAC                  VARCHAR(40)          not null,
   constraint PK_TBLDEVICE primary key (DEVICE_ID)
);

/*==============================================================*/
/* Index: TBLDEVICE_PK                                          */
/*==============================================================*/
create unique index TBLDEVICE_PK on TBLDEVICE (
DEVICE_ID
);

/*==============================================================*/
/* Index: RTBLUSER_RTBLDEVICE_FK                                */
/*==============================================================*/
create  index RTBLUSER_RTBLDEVICE_FK on TBLDEVICE (
USER_ID
);

/*==============================================================*/
/* Table: TBLNOTIFICATIONS                                      */
/*==============================================================*/
create table TBLNOTIFICATIONS (
   NOTIFICATION_ID      SERIAL               not null,
   DATA_ID              INT4                 not null,
   TITLE                VARCHAR(50)          not null,
   MESSAGE              VARCHAR(100)         not null,
   DATE_NOTIFICATION    DATE                 not null,
   constraint PK_TBLNOTIFICATIONS primary key (NOTIFICATION_ID, MESSAGE)
);

/*==============================================================*/
/* Index: TBLNOTIFICATIONS_PK                                   */
/*==============================================================*/
create unique index TBLNOTIFICATIONS_PK on TBLNOTIFICATIONS (
NOTIFICATION_ID,
MESSAGE
);

/*==============================================================*/
/* Index: RTBLDATA_TBLNOTI_FK                                   */
/*==============================================================*/
create  index RTBLDATA_TBLNOTI_FK on TBLNOTIFICATIONS (
DATA_ID
);

/*==============================================================*/
/* Table: TBLUSER                                               */
/*==============================================================*/
create table TBLUSER (
   USER_ID              SERIAL               not null,
   NAME                 VARCHAR(100)         not null,
   LAST_NAME            VARCHAR(100)         not null,
   EMAIL                VARCHAR(100)         not null,
   PASSWORD             VARCHAR(100)         not null,
   ADDRESS              VARCHAR(100)         not null,
   TYPE                 VARCHAR(100)         not null,
   IMGUSER              TEXT                 not null,
   REGISTRATIONDATE     DATE                 not null,
   DATEUPDATE           DATE                 not null,
   BIRTHDAYDATE         DATE                 not null,
   constraint PK_TBLUSER primary key (USER_ID)
);

/*==============================================================*/
/* Index: TBLUSER_PK                                            */
/*==============================================================*/
create unique index TBLUSER_PK on TBLUSER (
USER_ID
);

alter table TBLDATA
   add constraint FK_TBLDATA_RTBLDIVIC_TBLDEVIC foreign key (DEVICE_ID)
      references TBLDEVICE (DEVICE_ID)
      on delete restrict on update restrict;

alter table TBLDEVICE
   add constraint FK_TBLDEVIC_RTBLUSER__TBLUSER foreign key (USER_ID)
      references TBLUSER (USER_ID)
      on delete restrict on update restrict;

alter table TBLNOTIFICATIONS
   add constraint FK_TBLNOTIF_RTBLDATA__TBLDATA foreign key (DATA_ID)
      references TBLDATA (DATA_ID)
      on delete restrict on update restrict;

