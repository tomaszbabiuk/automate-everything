Powerful automation engine
==========================

Automating with "Automate-everything" is simple as dragging and dropping. The project is using [Google Blockly](https://developers.google.com/blockly) to make it simple and type-safe. No more scripting!

## The automation is type-safe. 

All automation blocks are strongly typed! This prevents failures because the user is not able to mix incompatible blocks together, for example: watt with celsius or others, conditions with triggers, etc... 

## The automation can be extended by other developers
The developers are welcome to create custom blocks that are reusable across other plugins!

![Automation](screenshot_objects_automation.png)

## Define the goal... leave the details for "Automate-everything"
The automation works on two levels:
1. The Object level (hardcoded, controlled by the programmer)
2. The Automation Blocks level (configured by the user)

The Object level automation is created entirely in the code by the Developers. That code is necessary for the object to function properly. The sky is the limit here but the main goal is to keep devices in consistent state.

The Automation Blocks level is totally configured by the user.

The great example are state devices. The Developer is defining the states, while the user is defining the conditions he/she wants the devices to be in that states.


### Example 1: A normally closed thermal actuator. 
A normally closed thermal actuators work as a valves that's open slowly when powered. The opening of the valve usually takes around two minutes. It would be hard for the user to track what's the opening level in automation blocks. That's why the object level code controls that aspect while the user can still configure when the actuator should be open or closed (without digging into the details of the operation).

### Example 2: A gate or a garage door.
A garage door drive mechanism is usually controlled by a button (or a remote controller). When the button is pressed, the gate is open (if closed) or closed (if open). The automated gate object from Automate Everything is using a signal from the magnetic/reed switch. The relay is simulating the action of pressing the button by shorting the contacts for one second. The user can define when the gate should be open or closed (without digging into the details of the operation as in the previous example).

## Two different ways of triggering the automation
The automation can be triggered on an event (like the state of another device changes) or in the loop.
Loop-based automation is better when device is controlled by "Automate-everything" only. You don't need an event to recalculate the automation state (the loop recalculates periodically).
Event-based automation fits better if device must be controlled manually and by "Automate-everything". See the table below for details:

|                  |Loop based automation|Event based automation|
|------------------|---------------------|----------------------|
|*Latency*         |Reacts according to time intervals, like every 5 seconds|Reacts immediately after event is fired|
|*Manual operation*|Changing the state of a device manually can bring some problems. Manual operation can disrupt the logic of the loop. Let's say you want the light to be on between 22:00 and 23:00. When you disable it manually ar 22:30, the loop will turn it back on when it reevaluates.|Manual operation is allowed. The manual operations are just another source of events.|
|*Power outage*    |The loop reevaluates and all devices are working as expected|Not reliable. The events may be omitted when no power.|
|*When to use*     |Use for every device that need to preserve power outages (for all critical aspects of your project like central heating, alarm system, ventilation).|Use for devices that can be manually triggered like lamps or gates|

## The Object is a single source of truth for its automation
You cannot automate object "A" by configuring the automation blocks of object "B" but you can have multiple loops or events controlling the same object. This keeps the automation clean and easy to debug. 