Powerful automation engine
==========================

Creating an automation with "Automate-everything" is simple as dragging and dropping. The project is using [Google Blockly](https://developers.google.com/blockly) engine intensively to achieve that.

## The automation is type-safe. 

All automation blocks are strongly typed! This prevents failures because the user is not able to mix for example: watts with celsius or other parameters that makes no sense when used incorrectly. 

## The automation can be extended by other developers
The developers are welcome to create custom blocks that can be than reused across other plugins!

![Automation](screenshot_objects_automation.png)

## Define the goal... leave the details for "Automate-everything"
The automation works on two levels:
1. The Object level (hardcoded, controlled by the programmer)
2. The Automation Blocks level (configured by the user)

The Object level automation is created entirely in the code by the Developers. That code is necessary for the object to function properly. The sky is the limit here but the main goal is to keep devices in consistent state.

The Automation Blocks level is totally controlled by the user.

The great example are state devices. The Developer is defining the states, while the user is defining the conditions he/she wants the devices to be in that states.


### Example
Imagine a normally closed thermal actuator. It works as a valve that's slowly opening when powered. It can even take two minutes! It would be hard for the user to track what's the opening level in automation blocks. That's why the object level code is controlling that aspect and the user can define when the actuator should be open or closed (without digging into the details of operation).

## The automation blocks can be event-based or a loop-based
The automation can be triggered on an event (like changing the state of a device) or in a loop.
Event-based automation can be better if device can be controlled manually and by "Automate-everything" at the same time.
Loop-based automation is better when device is controlled by "Automate-everything" only.
See [the difference](Loop-vs-event.md)

## The Object is a single source of truth for its automation
You cannot automate object "A" by configuring the automation blocks of object "B" but you can have multiple loops or events controlling the same object. This keeps the automation clean and easy to debug. 