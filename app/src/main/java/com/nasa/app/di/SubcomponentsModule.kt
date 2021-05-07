package com.nasa.app.di

import com.nasa.app.ui.activity.di.ActivityComponent
import dagger.Module

@Module(subcomponents = [ActivityComponent::class])
class SubcomponentsModule