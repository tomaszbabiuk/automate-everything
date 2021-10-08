package eu.automateeverything.rest

import java.lang.RuntimeException

class MappingException(from: Class<*>, to: Class<*>) :
    RuntimeException("Cannot map from: " + from.name + " to " + to.name)