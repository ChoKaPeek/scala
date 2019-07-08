package com.gkatzioura.eager

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import tools._
import com.google.inject.{Inject, Singleton}

@Singleton
class StartUpService {
    Consumer.consumeFromKafka("logs")
}

// A Module is needed to register bindings
class EagerLoaderModule extends AbstractModule {
    override def configure() = {
        bind(classOf[StartUpService]).asEagerSingleton
    }
}