#!/bin/bash

LOG_FILE="build.log"

SWING_UI_DIR="SwingUI"
CORE_DIR="Core"

BUILD_DIR="build"

TARGET="/target"
LIB_FILES="${TARGET}/modules/"
CLASS_FILES="${TARGET}/classes/"

SCRIPT_NAME="swift-rift.sh"

# DO NOT EDIT ANYTHING BELOW THIS LINE

_log() {
  printf '[%s] %s\n' $(date '+%d_%m_%Y-%H_%M_%S') "$*";
}

_debug() {
  if [ $# -eq 0 ]
  then
    while read -r data; do
        _log "${data}" | tee -a ${LOG_FILE}
    done
  else
    _log "$*" | tee -a ${LOG_FILE}
  fi
}

rm "${LOG_FILE}"
_debug "CLEANING UP OLD TARGET"
rm -rfv "${BUILD_DIR}" "${SWING_UI_DIR}${TARGET}" "${CORE_DIR}${TARGET}"| _debug
_debug "CLEANING PROJECT"
mvn clean | _debug
_debug "INSTALLING PROJECT"
mvn install | _debug
_debug "COMPILING PROJECT"
mvn compile | _debug
_debug "CREATING BUILD DIRECTORY"
mkdir -v "${BUILD_DIR}" | _debug
_debug "CONFIGURING SCRIPT"
cp --verbose "${SWING_UI_DIR}${LIB_FILES}"* "${BUILD_DIR}" | _debug
cp --verbose -R "${SWING_UI_DIR}${CLASS_FILES}"* "${BUILD_DIR}" | _debug
CLASSPATH=$(find ~+/${SWING_UI_DIR} -name '*.jar' | tr '\n' ':')
echo "java -classpath '$(pwd)/${BUILD_DIR}:${CLASSPATH:0:-1}' com.hawolt.LeagueClientUI \"\$@\"" > ${SCRIPT_NAME}
chmod +x "${SCRIPT_NAME}"
_debug "COMPLETE"
