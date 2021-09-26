package eu.automateeverything.domain.firstrun

import java.io.File

class FileCheckingFirstRunService() : FirstRunService {
    companion object {
        const val MARK_FILE_NAME = "firstrun.marker"
    }

    private val markingFile = File(MARK_FILE_NAME)

    override fun isFirstRun(): Boolean {
        return !markingFile.exists()
    }

    override fun markFirstRunHappened() {
        markingFile.createNewFile()
    }
}