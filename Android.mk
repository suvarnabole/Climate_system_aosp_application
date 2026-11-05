# AOSP Vehicle Climate Application Make File (Android.mk)

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ClimateApplication
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under app/src/main/java)
LOCAL_AIDL_FILES := $(call all-aidl-files-under app/src/main/aidl)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/app/src/main/res
LOCAL_MANIFEST_FILE := app/src/main/AndroidManifest.xml

LOCAL_SDK_VERSION := system_current
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_STATIC_JAVA_LIBRARIES := \
    androidx.core_core-ktx \
    androidx.lifecycle.lifecycle-runtime-ktx \
    androidx.activity.activity-compose \
    androidx.compose.material3_material3 \
    androidx.compose.ui_ui \
    androidx.compose.ui_ui-graphics \
    androidx.compose.ui_ui-tooling-preview \
    kotlinx-coroutines-android

include $(BUILD_PACKAGE)
