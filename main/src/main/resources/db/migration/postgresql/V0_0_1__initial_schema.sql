create sequence hibernate_sequence start 1 increment 1;

create table bridge
(
	id          int8         not null,
	hueusername varchar(255),
	ip          varchar(255) not null,
	user_id     int8,
	primary key (id)
);

create table job
(
	id      int8         not null,
	name    varchar(255) not null,
	lamp_id int8,
	primary key (id)
);

create table lamp
(
	id                int8         not null,
	hueuniqueid       varchar(255) not null,
	lastshownscenario varchar(255),
	name              varchar(255) not null,
	workingend        time         not null,
	workingstart      time         not null,
	team_id           int8         not null,
	primary key (id)
);

create table scenarioconfig
(
	id                                 int8         not null,
	brightness                         int4         not null,
	brightnesschangeenabled            boolean      not null,
	colorchangeenabled                 boolean      not null,
	colorhex                           varchar(255),
	lampon                             boolean      not null,
	onetimepulsationcolorchangeenabled boolean      not null,
	onetimepulsationcolorhex           varchar(255),
	onetimepulsationenabled            boolean      not null,
	scenario                           varchar(255) not null,
	lamp_id                            int8,
	primary key (id)
);

create table team
(
	id   int8         not null,
	name varchar(255) not null,
	primary key (id)
);

create table team_scenariopriority
(
	team_id          int8 not null,
	scenariopriority varchar(255)
);

create table user_
(
	id       int8         not null,
	email    varchar(255),
	forename varchar(255),
	login    varchar(255) not null,
	surname  varchar(255),
	team_id  int8         not null,
	primary key (id)
);

create table user_roles
(
	user_id int8 not null,
	roles   varchar(255)
);

alter table lamp
	add constraint uk_lamp_hue_unique_id unique (hueuniqueid);

alter table team
	add constraint uk_team_name unique (name);

alter table bridge
	add constraint fk_bridge_user_id foreign key (user_id) references user_;

alter table job
	add constraint fk_job_lamp_id foreign key (lamp_id) references lamp;

alter table lamp
	add constraint fk_lamp_team_id foreign key (team_id) references team;

alter table scenarioconfig
	add constraint fk_scenario_config_lamp_id foreign key (lamp_id) references lamp;

alter table team_scenariopriority
	add constraint fk_team_scenario_priority_team_id foreign key (team_id) references team;

alter table user_
	add constraint fk_user_team_id foreign key (team_id) references team;

alter table user_roles
	add constraint fk_user_roles_user_id foreign key (user_id) references user_;