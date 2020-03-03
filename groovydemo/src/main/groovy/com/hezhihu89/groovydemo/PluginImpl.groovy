package com.hezhihu89.groovydemo

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginImpl implements Plugin<Project> {

    Project mProject

    void apply(Project project) {
        println("初始化插件： " + project.plugins.hasPlugin(AppPlugin))
        this.mProject = project
        BaseExtension android
        if(project.plugins.hasPlugin(AppPlugin)){
            android = project.extensions.getByType(AppExtension.class)
            println("添加APP插件")
        }else if(project.plugins.hasPlugin(LibraryPlugin.class)){
            android = project.extensions.getByType(LibraryExtension.class)
            println("添加lib插件")
        }
        if(null != android) {
            android.registerTransform(new DemoTransform(project))
        }
    }
}
