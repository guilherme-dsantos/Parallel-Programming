package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class ActorHierarchyExperiments {
    public static void main(String[] args) {
        ActorRef<String> testSystem = ActorSystem.create(StartStopActor1.create(), "testSystem");
        testSystem.tell("stop");
    }
}