create sequence hibernate_sequence start 1 increment 1;

create table bridge
(
	id            int8         not null,
	hue_user_name varchar(255),
	ip            varchar(255) not null,
	user_id       int8,
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
	id                  int8         not null,
	hue_unique_id       varchar(255) not null,
	last_shown_scenario varchar(255),
	name                varchar(255) not null,
	working_end         time         not null,
	working_start       time         not null,
	team_id             int8         not null,
	primary key (id)
);

create table scenario_config
(
	id                                     int8         not null,
	brightness                             int4         not null,
	brightness_change_enabled              boolean      not null,
	color_change_enabled                   boolean      not null,
	color_hex                              varchar(255),
	lamp_on                                boolean      not null,
	onetime_pulsation_color_change_enabled boolean      not null,
	onetime_pulsation_color_hex            varchar(255),
	onetime_pulsation_enabled              boolean      not null,
	scenario                               varchar(255) not null,
	lamp_id                                int8,
	primary key (id)
);

create table team
(
	id   int8         not null,
	name varchar(255) not null,
	primary key (id)
);

create table team_scenario_priority
(
	team_id           int8 not null,
	scenario_priority varchar(255)
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
	add constraint UK_4ayls853w3sxsrsliv90jx1sm unique (hue_unique_id);

alter table team
	add constraint UK_g2l9qqsoeuynt4r5ofdt1x2td unique (name);

alter table bridge
	add constraint FKhv022q711scdua3nntbd8u7b4 foreign key (user_id) references user_;

alter table job
	add constraint FK3g12tqftcffdlj74bhiudarso foreign key (lamp_id) references lamp;

alter table lamp
	add constraint FKh6gu0nysainmtb88eseprnve3 foreign key (team_id) references team;

alter table scenario_config
	add constraint FKbuebplb49k0xylduyhrhxlj1k foreign key (lamp_id) references lamp;

alter table team_scenario_priority
	add constraint FKjl1ilcsnnvdfmkeqdy5hmgyry foreign key (team_id) references team;

alter table user_
	add constraint FK519dyqrgi44n8mctrtwbsw9ww foreign key (team_id) references team;

alter table user_roles
	add constraint FKi11ca3b26vh3bcucnq39inil3 foreign key (user_id) references user_;