# Zero-conf principle

Nobody likes configuration. That's why Automate Everything is following "Zero-conf" principle.

The general approach is that *if something can be configured automatically - it will be added automatically* to the system.

Let's see the examples of Zero-conf principle.

### Example 1: "Shelly" devices
_All "Shelly" devices that are in your network will be automatically hijacked and added to the system as ports. All you need to do is to connect them to local network, Automate Everything will take care of the rest._

### Example 2: "One-wire" devices
_Automate Everything is automatically scanning for all available serial ports on every restart. When there's a 1-wire adapter connected to any USB ports, it will be immediatelly queried and all matching devices will be exposed as ports._
