package com.thiefspin.kafka.client.producer

import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.{ActorRef, ActorSystem}
import com.thiefspin.kafka.client.actor.WorkerSupervisor
import com.thiefspin.kafka.client.json.DefaultJsonFormatter
import com.thiefspin.kafka.client.message.{Produce, WorkerSupervisorMessage}
import com.thiefspin.kafka.client.producer.impl.ApacheKafkaProducer
import play.api.libs.json.Format

class SimpleKafkaProducerClient(producerType: KafkaProducerType, ref: ActorRef[WorkerSupervisorMessage]) extends KafkaProducerClient(producerType, ref) {

  def produce[A](topic: String, msg: A)(implicit format: Format[A]): Unit = {
    super.produce[A](topic, msg)(DefaultJsonFormatter[A]())
  }

  def produce(topic: String, msg: String): Unit = {
    ref ! Produce(topic, msg, producerType)
  }

}

object SimpleKafkaProducerClient {

  private def defaultProducer(servers: String): KafkaProducerType = ApacheKafkaProducer(None, Option(servers))(None).producer

  def apply[B](servers: String, system: ActorSystem[B]): KafkaProducerClient = {
    new SimpleKafkaProducerClient(defaultProducer(servers), WorkerSupervisor(system))
  }

  def apply[B](servers: String, context: ActorContext[B]): KafkaProducerClient = {
    new SimpleKafkaProducerClient(defaultProducer(servers), WorkerSupervisor(context))
  }
}