# JenkinsHue

This is the implementation of my IHK project (June 2016). It shows the state of multiple jenkins builds using Philips hue lights. The application can handle multiple lights und bridges (Philips products).

![screenshot1](https://github.com/fi3te/JenkinsHue/blob/master/doc/screenshot1.png)

## Requirements
- LDAP server
- Jenkins server
- Philips Hue bridge and light


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





