package com.thiefspin.kafka.client.message

import com.thiefspin.kafka.client.producer.SimpleKafkaProducer

sealed trait WorkerSupervisorMessage

final case class Produce(topic: String, msg: String, simpleKafkaProducer: SimpleKafkaProducer) extends WorkerSupervisorMessage

final case class Consume() extends WorkerSupervisorMessage