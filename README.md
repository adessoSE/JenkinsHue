# JenkinsHue [![Build Status](https://travis-ci.org/adessoAG/JenkinsHue.svg?branch=master)](https://travis-ci.org/adessoAG/JenkinsHue)

This is the implementation of my IHK project (Fiete Wennier, June 2016). It shows the state of multiple jenkins builds using Philips hue lamps. The application can handle multiple lamps und bridges (Philips products).

![screenshot1](https://github.com/fi3te/JenkinsHue/blob/master/doc/screenshot1.png)


## Getting started

### Requirements
- Jenkins server
- Philips Hue Bridge
- Philips Hue lamps (e.g. Philips Hue Go)
- LDAP server (recommended, not necessary)

### Usage
- Install [Docker Compose](https://docs.docker.com/compose/install/)
- Create a docker-compose.yml
- Run it with...
```sh
docker-compose up
```
- Visit [http://localhost:8484/](http://localhost:8484/)
- If no ldap connection is specified you can login with username = password (property 'admins')

1) docker-compose.yml with an in-memory database
```
version: '2'
services:
  jh:
    restart: always
    image: fi3te/jenkins-hue
    container_name: jh-app
    environment:
     - "TZ=Europe/Berlin"
     - jenkins.url=TODO
     - jenkins.userName=TODO
     - jenkins.password=TODO
   # OPTIONAL
   # - ldap.server.domain=
   # - ldap.server.url= e.g. ldaps://xxxxxxxxxxxxxxxxxxxxx.xxxxxxxxxxxxxxxxxxxxx.de
   # - ldap.server.userSearchFilter= e.g. (&(xxxxxxxxxxxxxxxxxxxxx={0})(objectclass=user))
   # - ldap.server.userSearchBase=
   # - ldap.server.groupSearchBase=
   # - ldap.server.userDn= e.g. CN=xxxxxxxxxxxxxxxxxxxxx,OU=xxxxxxxxxxxxxxxxxxxxx,DC=xxxxxxxxxxxxxxxxxxxxx
   # - ldap.server.userName=
   # - ldap.server.password=
   # - trust-store= e.g. filename.ending
   # - trust-store-password=
    ports:
     - "127.0.0.1:8484:8484"
```

2) docker-compose.yml with a PostgreSQL database
```
version: '2'
services: 
  postgres:
    restart: always
    image: postgres:10
    container_name: jh-postgres
    environment:
      POSTGRES_PASSWORD: postgres-password-example
    volumes_from:
     - data
  jh:
    restart: always
    image: fi3te/jenkins-hue
    container_name: jh-app
    environment:
     - "TZ=Europe/Berlin"
     - spring.profiles.active=postgre,live
     - spring.datasource.password=postgres-password-example
     - jenkins.url=TODO
     - jenkins.userName=TODO
     - jenkins.password=TODO
   # OPTIONAL
   # - ldap.server.domain=
   # - ldap.server.url= e.g. ldaps://xxxxxxxxxxxxxxxxxxxxx.xxxxxxxxxxxxxxxxxxxxx.de
   # - ldap.server.userSearchFilter= e.g. (&(xxxxxxxxxxxxxxxxxxxxx={0})(objectclass=user))
   # - ldap.server.userSearchBase=
   # - ldap.server.groupSearchBase=
   # - ldap.server.userDn= e.g. CN=xxxxxxxxxxxxxxxxxxxxx,OU=xxxxxxxxxxxxxxxxxxxxx,DC=xxxxxxxxxxxxxxxxxxxxx
   # - ldap.server.userName=
   # - ldap.server.password=
   # - trust-store= e.g. filename.ending
   # - trust-store-password=
    ports:
     - "127.0.0.1:8484:8484"
    depends_on:
     - postgres
  data:
    image: tianon/true
    container_name: jh-data
    volumes:
     - /var/lib/postgresql/data

```

### Bridge discovery

The bridge can be discovered automatically via N-UPnP. Therefore it needs an internet connection.

Otherwise you can also type in the IP address ot the bridge manually.

Further information can be found [here](https://developers.meethue.com/documentation/hue-bridge-discovery).


## How Does It Work

The application uses the REST-API of your Jenkins server and recognizes states changes of the observed builds. On the basis of the occured scenarios (multiple builds -> multiple scenarios) it decides which scenario should be shown (the scenarios can be prioritized). Possible scenarios are:

- BUILDING after FAILURE
- BUILDING after UNSTABLE
- BUILDING after SUCCESS
- FAILURE after SUCCESS
- FAILURE after UNSTABLE
- FAILURE after FAILURE
- UNSTABLE after SUCCESS
- UNSTABLE after FAILURE
- UNSTABLE after UNSTABLE
- SUCCESS after FAILURE
- SUCCESS after UNSTABLE
- SUCCESS after SUCCESS

You can also configure fallback scenarios:

- BUILDING
- FAILURE
- UNSTABLE
- SUCCESS

For each scenario you can define (colored) pulsation and color change.

![structure chart](https://github.com/fi3te/JenkinsHue/blob/master/doc/structure_chart.png)


## License

Code released under the MIT License.


## Disclaimer

The authors of this software are in no way affiliated to Philips. 
All naming rights for Philips and Philips Hue are property of Philips.


## Acknowledgements
### The Hue SDK by Philips

Philips releases this SDK with friendly house rules. These friendly house rules are part of a legal framework; this to protect both the developers and hue. The friendly house rules cover e.g. the naming of Philips and of hue which can only be used as a reference (a true and honest statement) and not as a an brand or identity. Also covered is that the hue SDK and API can only be used for hue and for no other application or product. Very common sense friendly rules that are common practice amongst leading brands that have released their SDK’s.

Copyright (c) 2012- 2013, Philips Electronics N.V. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

- Neither the name of Philips Electronics N.V. , nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOTLIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FORA PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER ORCONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, ORPROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OFLIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDINGNEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THISSOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
