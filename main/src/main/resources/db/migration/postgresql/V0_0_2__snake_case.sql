alter table scenarioconfig
	rename to scenario_config;
alter table team_scenariopriority
	rename to team_scenario_priority;

alter table bridge
	rename column hueusername to hue_user_name;

alter table lamp
	rename column hueuniqueid to hue_unique_id;
alter table lamp
	rename column lastshownscenario to last_shown_scenario;
alter table lamp
	rename column workingend to working_end;
alter table lamp
	rename column workingstart to working_start;

alter table scenario_config
	rename column brightnesschangeenabled to brightness_change_enabled;
alter table scenario_config
	rename column colorchangeenabled to color_change_enabled;
alter table scenario_config
	rename column colorhex to color_hex;
alter table scenario_config
	rename column lampon to lamp_on;
alter table scenario_config
	rename column onetimepulsationcolorchangeenabled to onetime_pulsation_color_change_enabled;
alter table scenario_config
	rename column onetimepulsationcolorhex to onetime_pulsation_color_hex;
alter table scenario_config
	rename column onetimepulsationenabled to onetime_pulsation_enabled;

alter table team_scenario_priority
	rename column scenariopriority to scenario_priority;