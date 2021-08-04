/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     3/8/2021 23:00:26                            */
/*==============================================================*/


drop index RTBLDIVICE_RTBLDATA_FK;

drop index TBLDATA_PK;

drop table TBLDATA;

drop index RTBLUSER_RTBLDEVICE_FK;

drop index TBLDEVICE_PK;

drop table TBLDEVICE;

drop index TBLUSER_PK;

drop table TBLUSER;

/*==============================================================*/
/* Table: TBLDATA                                               */
/*==============================================================*/
create table TBLDATA (
   DATA_ID              SERIAL               not null,
   DEVICE_ID            INT4                 not null,
   MQGAS                DECIMAL(5,2)         not null,
   MLX                  DECIMAL(5,2)         not null,
   MQHUMO               DECIMAL(5,2)         not null,
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

