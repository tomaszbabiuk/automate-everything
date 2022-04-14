# Automate Everything is an operating system for every automation project

Automate Everything does not limit to home automation only. As regular operating system provides drivers and resources (memory and cpu) to programs, Automate Everything provides ports and services for plugins. By selecting a proper set of plugins you can perform different automation tasks.

## Ports

The port represents a way to capture a measurable value (like temperature) or control an actuator (like relay, triack or transistor). The ports in "Automate Everything" are used to communicate the software with physical world. They can provide temperature, humidity, a relay, binary inputs or any other custom values. Just like USB and HDMI ports in your PC or Mac.

The abstraction of ports is crucial when you want to mix devices coming from different manufacturers. "Automate Everything" takes care that thermometer being produced by company "A" can control the relay from company "B" using the settings from controller "C". You can mix the signals coming from different devices using completely different technologies (like turning a WiFi based device accorting to the temperature reported by Zigbee thermometer).

The system is totally flexible when it comes to the types of ports. Let's say that you want to use "Automate Everything" for home automation, you'll need ports like: temperature, humidity, relays and binary inputs. Another example could be a system that monitors air pollution. In this case you would need a plugin that provides a set of ports for air quality metrics (like PM10, PM2.5). The data coming from air quality ports can be used to turn air purifiers on or execute any other actions.

The plugins can always define its custom ports. The best example is "crypto-trading" plugin that defines a "Ticker" port that can be used to trade cryptocurrencies.

## Services

The services are system functionalities that Automate Everything share with the plugins. At the moment of writing this documentation those services are:
 - Port finder - this service is responsible for looking up the ports
 - Events sink - is used to share the events across the system that something important has happened like a port has updated its value, an inbox message has been sent or the heart of the system is still beating
 - Inbox - this service allows the plugins to leave messages that can later be read in the mailbox
 - State change reporter - that service takes care that all the state changes are reported on time and devices can take actions based when state of other device has changed
 - MQTT broker service - gives access to embedded MQTT broker
 - LAN Gateway resolver - this service is obtaining a local IP address of the system, which is extremely handful communicating devices in LAN network together

## Objects

Automate Everything is using "objects" to model physical devices. The objects can access the ports and also the services. You can think of the objects like a small programs that do execute the code and connect to the ports. The objects can have multiple states and user can define custom automations that will trigger those states.

The example of objects: 
 - Circulation pump that controls the relay turning the physical pump on and off and a thermometer that measures the temperature after the pump. Circulation pump turns the pump on as long as temperature is rising (which means the water in the pipes is cold). When the temperature stabilizes, the pump is turned off. This was the pump is not working all the time and the energy for powering the pump is saved. After some time... the cycle repeats. You can define a custom automation blocks that will decide when the pumping cycle should be active (like every morning) but you don't need to specify the details of pumping (measuring the temperature, calculating temperature deltas and so on).
 
 - Timed On/Off device. This is a simple object that controls the relay. It contains few additional parameters like maximum working time, break time, etc. User can define automation blocks that turn the device on and off but the logic that turns the object off (if maximum working time is reached) is contained in the object.

 - Alarm line and alarm zones. Those objects can emulate a complete alarm system. Alarm zone is watching the state of alarm lines and fires the alarm if any line is breached and the zone is armed. The user can define when the zone is armed but all the logic for keeping the lines in sync is done by the objects.

The objects are really powerful. They model physical devices. The user can specify when the object should do its job but the actual details of operation is handled by the object. In other words: the user is the employer telling the employee what and when he should do.
