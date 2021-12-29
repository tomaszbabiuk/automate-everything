import vuetify from './plugins/vuetify'

const lang = vuetify.framework.lang

export function formatTimeAgo(now, timestamp) {

    var totalSeconds =  Math.round((now - timestamp)/1000);
    if (totalSeconds < 0) {
      return "---"
    } 

    if (totalSeconds < 60) {
     return lang.t("$vuetify.ago.s", totalSeconds);
    } 
    
    var totalMinutes = Math.round(totalSeconds / 60);
    
    if (totalMinutes < 60) {
      return lang.t("$vuetify.ago.m", totalMinutes);  
    }

    var totalHours = Math.round(totalSeconds / 3600);
    var minutesModulo = totalMinutes % 60;
    if (totalHours < 24) {
      return lang.t("$vuetify.ago.h", totalHours, minutesModulo);
    }

    var totalDays = Math.round(totalSeconds / 86400)
    var hoursModulo = totalHours % 24;

    return lang.t("$vuetify.ago.d", totalDays, hoursModulo)
  }