//
// Created by ASUS on 2023/11/25.
//

#include "jni.h"
#include <fcntl.h>
#include <termios.h>

JNIEXPORT jobject JNICALL
Java_com_example_SerialPortUtils_open(JNIEnv *env, jclass type, jstring path, jint baudRate) {
    const char *path_utf = (*env)->GetStringUTFChars(env, path, NULL);
    int fd = open(path_utf, O_RDWR);
    (*env)->ReleaseStringUTFChars(env, path, path_utf);
    if (fd == -1) {
        return NULL;
    }

    struct termios options;
    tcgetattr(fd, &options);
    speed_t speed;
    switch (baudRate) {
        case 9600:
            speed = B9600;
            break;
        case 19200:
            speed = B19200;
            break;
        case 38400:
            speed = B38400;
            break;
        case 57600:
            speed = B57600;
            break;
        case 115200:
            speed = B115200;
            break;
        default:
            speed = B9600;
    }
    cfsetispeed(&options, speed);
    cfsetospeed(&options, speed);

    // 8N1
    options.c_cflag &= ~PARENB;
    options.c_cflag &= ~CSTOPB;
    options.c_cflag &= ~CSIZE;
    options.c_cflag |= CS8;

    // 无硬件流控制
    options.c_cflag &= ~CRTSCTS;

    // 可读可写
    options.c_cflag |= CREAD | CLOCAL;

    // 无软流控制
    options.c_iflag &= ~(IXON | IXOFF | IXANY);

    // 非规范模式
    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);

    // 设置等待时间和最小接收字符
    options.c_cc[VTIME] = 1;
    options.c_cc[VMIN] = 1;

    tcflush(fd, TCIOFLUSH);
    tcsetattr(fd, TCSANOW, &options);

    jobject mFileDescriptor;
    jclass mFileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");
    jmethodID mFileDescriptorConstructor = (*env)->GetMethodID(env, mFileDescriptorClass,
                                                               "<init>", "()V");
    jfieldID mFileDescriptorDescriptor = (*env)->GetFieldID(env, mFileDescriptorClass,
                                                            "descriptor", "I");
    mFileDescriptor = (*env)->NewObject(env, mFileDescriptorClass, mFileDescriptorConstructor);
    (*env)->SetIntField(env, mFileDescriptor, mFileDescriptorDescriptor, (jint) fd);

    return mFileDescriptor;
}

JNIEXPORT void JNICALL
Java_com_example_SerialPortUtils_close(JNIEnv *env, jobject instance) {
    jclass mFileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");
    jfieldID mFileDescriptorDescriptor = (*env)->GetFieldID(env, mFileDescriptorClass,
                                                            "descriptor", "I");
    jint descriptor = (*env)->GetIntField(env, instance, mFileDescriptorDescriptor);
    close(descriptor);
}
