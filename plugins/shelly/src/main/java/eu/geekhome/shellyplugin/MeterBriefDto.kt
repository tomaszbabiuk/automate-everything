package eu.geekhome.shellyplugin

/*
   "meters":[
      {
         "power":0.00,
         "is_valid":true,
         "timestamp":1575191987,
         "counters":[
            0.000,
            0.000,
            0.000
         ],
         "total":0
      }
   ]
 */
data class MeterBriefDto(

    val power: Double
)