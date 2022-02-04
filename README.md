# Welcome
"Automate Everything" is a multipurpose automation server. It can control your home or garden watering system. It can even trade cryptocurrencies! 

Think of "Automate Everything" as more plugin friendly OpenHab or deeply customizable HomeAssistant. You can even use "Automate Everything" as "If This Than That" service in your local network. 

With correct set of plugins you can automate literally everything!

# Best features of "Automate Everything"
1. [An operating system for automation projects](doc/features/OS-for-automation-projects.md)
2. [Everything is a plugin](doc/features/Everything-is-a-plugin.md)
3. [Zero-conf principle](doc/features/Zero-conf-principle.md)
4. [Modern UI](doc/features/Modern-UI.md)
5. [Powerful automation engine](doc/features/Powerful-automation-engine.md)
6. [Developers haven](doc/features/Developers-haven.md) 
7. [Embedded MQTT server](doc/features/Embedded-mqtt-server.md)

# A note from the author
The work on "Automate Everything" was started in 2012. Yes... almost 10 years ago!. At first the project was called "geekHOME Server". In the beginning geekHOME was written in C# Micro Framework (it was running on a dedicated microcontroller called "FEZ Cobra"!). It's been the beginning of DIY Home Automation: open source projects like OpenHAB was barely starting. Some other projects (that are now open-sourced) were paid, so I decided to create something on my own.
In 2015 the code was ported to Java to be able to run on Raspberry Pi 1. It was able to control the lights, central heating, alarm and ventilation at my own house. It has never been published. Comparing to FEZ Cobra, Raspberry Pi was 5x cheaper and 100x more powerful.

In 2020, I decided to give the project another shot. "Automate Everything" is now a full-blown automation server. Everything has been rewritten (only the original concept hasn't changed).

I didn't focus on Home Automation this time... I wanted to create an "Operating System" for every type of automation project. I wanted to create something that will be really easy to extend by the others. That's why every single aspect of "Automate Everything" can be controlled or extended by plugins. I hope the project will be useful for the other "DIY Tinkerers" like me.


# State of the project
The project is still in development. I plan to do a full release by the end od 2022 but you are welcome to try it out now. 
If you find this project useful, please share with the others!

# How to...
[Quick install on Raspberry Pi](doc/howtos/Quick-install-on-Raspberry-Pi.md)

# For Developers and Contributors
[Installing from sources on Raspberry Pi](doc/dev/Installing-from-sources-on-Raspberry-Pi.md)
[Logging](doc/dev/Logging.md)
