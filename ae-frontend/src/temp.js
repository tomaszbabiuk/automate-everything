export const temp = {
  CELSIUS: { title: "°C", code: "c" },
  KELVIN: { title: "K", code: "k" },
  FAHRENHEIT: { title: "°F", code: "f" },

  obtainTemperatureUnit: function () {
    var storedUnit = localStorage.temperatureUnit
    if (!storedUnit) {
      return this.CELSIUS
    } else {
      return JSON.parse(storedUnit)
    }
  },

  storeTemperatureUnit: function(unit) {
    localStorage.temperatureUnit = JSON.stringify(unit)
  },

  kelvinsToDisplayTemperature: function (tempInK) {
    if (tempInK) {
      var temperatureUnit = this.obtainTemperatureUnit().code
      var currentTemp = Number(tempInK);
      var newTemp = 0;
      if (temperatureUnit === "c") {
        newTemp = currentTemp - 273.15;
      }

      if (temperatureUnit === "f") {
        newTemp = currentTemp * 9/5 - 459.67;
      }

      if (temperatureUnit === "k") {
        newTemp = currentTemp;
      }

      return newTemp.toFixed(2);
    }

    return null;
  },

  displayTemperatureToKelvins: function (displayTemp) {
    if (displayTemp) {
      var temperatureUnit = this.obtainTemperatureUnit().code
      var valueInK;
      var number = Number(displayTemp);

      if (temperatureUnit === "c") {
        valueInK = number + 273.15;
      }

      if (temperatureUnit === "f") {
        valueInK = (number - 32) / 1.8 + 273.15;
      }

      if (temperatureUnit === "k") {
        valueInK = number;
      }

      return valueInK;
    } else {
      return null;
    }
  }
}