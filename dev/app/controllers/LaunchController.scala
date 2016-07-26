package controllers

import play.api.mvc._
import play.api._
import play.api.i18n._
import javax.inject.Inject
import models._
import play.api.data._
import play.api.data.Forms._
import scala.sys.process._
import java.util.Date
import java.io._

class LaunchController @Inject() (val messagesApi:MessagesApi) extends Controller with I18nSupport  {
    
  private val BwaForm : Form[Bwa]=Form(mapping(                        
                                                "job_name" -> nonEmptyText,
                                                "option1" -> nonEmptyText)(Bwa.apply)(Bwa.unapply)
                                                      ) 
 
  
  def home= Action{
    implicit request =>
      Ok(views.html.home())
  }
  
  def analysis = Action{
     implicit request =>
       Ok(views.html.bwa())
  }
  def launch =Action{
    implicit request =>
      val bwa=BwaForm.bindFromRequest()
       bwa.fold(
           hasErrors=> Ok("aaa")
           , success=> {           
                  // val job_name ="bwa-"+bwa.data("job_name")+"-"+(new Date).toString()
                  // val cmd =Seq("mkdir", "/home/user1/"+job_name)
                  // Process(cmd).run
                    //  cd data to directory
             ///////////////adduser + create directory +Dockerfile,innersh.sh + chmod//////////////////////
                          var cmd = "./"
                          bwa.data.keys.foreach{
                                  option => cmd+=bwa.data(option)+" "
                              }
                          cmd+="MT.fa"
                          writingInnerSh("/home/user1/bwa", cmd)
                          writingDockerfile("/home/user1/bwa")
                          Process("sudo chmod 777 /home/user1/bwa").run
                          Process("sudo chmod 777 /home/user1/bwa/Dockerfile").run
                          Process("sudo chmod 777 /home/user1/bwa/innerSh.sh").run
                          val launch =Seq("launch.sh","/home/user1/bwa",bwa.data("job_name")+"-"+(new Date).toString())
                          Process(launch).run()
                          Ok(views.html.result(bwa.data))
           }
           )
  }  
  
   private def writingDockerfile(filePath :String) = {
    val bw = new BufferedWriter(new FileWriter(filePath+"/Dockerfile"))
     bw.write("FROM ubuntu:latest")
     bw.newLine()
     bw.write("ADD innerSh.sh /tmp")/////추후수정 nfs쓰면 add안해줌
     bw.newLine()
     bw.write("ADD MT.fa /tmp")////ddd
     bw.newLine()
     bw.write("WORKDIR /tmp")
     bw.newLine()
     bw.write("CMD ./innerSh.sh")
     bw.close()
  }              
   private def writingInnerSh(filePath:String, cmd:String)={
     val bw = new BufferedWriter(new FileWriter(filePath+"/innerSh.sh"))
     bw.write("#! /bin/sh")
     bw.newLine()
     bw.write(cmd)
    // bw.newLine()
     /////////curl 추가 
     bw.close()
   }
  
  
}