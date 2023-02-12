# Welcome
"Automate Everything" is a multi-purpose automation framework. It's an open source, vendor independent and technology-agnostic approach to rule-based automation.

At first glance "Automate Everything" is very similar to other open-source automation platforms (see similar projects) but it's built on top of [objects](doc/features/Powerful-automation-engine.md) instead of bindings.
It's not limited to smart homes only. It's very [extensible](doc/features/Everything-is-a-plugin.md) and [easy](doc/features/Modern-UI.md) to use.

# What can you build?
The sky is the limit... you can use it to automate literally everything! If you're interested in any type of projects below, contact me on [Linked-In](https://www.linkedin.com/in/tomasz-babiuk/).

### Smart building
- A smart home
- An energy consumption management system
- A central heating controller
- An alarm system
- A local If-This-Than-That engine

### Smart gardening
- A smart glasshouse
- Watering system
- Hydroponic controller

### Trading
- A micro hedging fund
- Trading alerts
- Smart trading platform

### Industrial automation
- Access control system
- A mix of access control and home automation system
- Industrial-grade control panels

# Similar projects
1. OpenHAB
2. HomeAssistant
3. Domoticz 
4. NodeRED

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
The work on "Automate Everything" started in 2012. Yes... more than 10 years ago!. In the beginning the project was called "geekHOME Server". It was written in C# Micro Framework (it was running on a dedicated microcontroller called "FEZ Cobra"!). It's been the beginning of DIY Home Automation: open source projects like OpenHAB was barely starting. Some other projects (that are now open-sourced) were paid, so I decided to create something on my own.
In 2015 the code was ported to Java to be compatible with Raspberry Pi single board computer. It was able to control lights, central heating, alarm and ventilation at my own house. It has never been published.

In 2020, I decided to give the project another shot. "Automate Everything" is now a full-blown automation server. Everything has been rewritten (the original concept is unchanged).

I didn't focus on Home Automation this time... I wanted to create an "Operating System" for every type of automation project. I wanted to create something that will be really easy to extend by the others. That's why every single aspect of "Automate Everything" can be controlled or extended by plugins. I hope the project will be useful for the other "DIY Tinkerers" like me.


# State of the project
The project is still in development. I plan to do a full release by the end od 2022 but you are welcome to try it out now. 
If you find this project useful, please share with the others!

# How to...
- [Quick installation on Raspberry Pi](doc/howtos/Quick-install-on-Raspberry-Pi.md)

# For Developers and Contributors
- [Installing from sources on Raspberry Pi](doc/dev/Installing-from-sources-on-Raspberry-Pi.md)
- [Logging](doc/dev/Logging.md)
