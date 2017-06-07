var express=require('express');
var bodyParser = require('body-parser');
var fb=require('./firebase.js');
var fs = require('fs');
var app=express();

app.use(bodyParser.urlencoded({ extended: true, limit: '50mb' }));
app.use(bodyParser.json());
app.use(bodyParser.json({limit: '50mb'}));

var multer=require('multer');
var name;
//multer settings
var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/')
  },
  filename: function (req, file, cb) {
    name = file.fieldname + '-' + Date.now();
    cb(null, name);
  }
})
var upload = multer({ storage: storage })

//login
app.post('/login',upload.single('param'),function(req,res){

  var d0=req.body.param[0]; //id
  var d1=req.body.param[1]; //password

  console.log("ID : "+d0+" is trying to login with PW: "+d1);
  fb.login(d0,d1,function(response){
    switch(response){
      case 0:
        console.log("Login success");
        break;
      case 1:
        console.log("Wrong password");
        break;
      case 2:
        console.log("No such ID");
        break;
    }
    res.end(response.toString());

  });
});

//register
app.post('/register',upload.single('param'),function(req,res){
  var d0=req.body.param[0]; //id
  var d1=req.body.param[1]; //name
  var d2=req.body.param[2]; //password
  var d3=req.body.param[3]; //password_confirm

  fb.register(d0,d1,d2,d3,function(response){
    switch(response){
      case 0:
        console.log("ID :"+d0+" has registered");
        break;
      case 1:
        console.log("ID :"+d0+" already exists");
        break;
      case 2:
        console.log("Password not match");
        break;
    }
    res.end(response.toString());
  });
});

//docSend
app.post('/docSend',upload.single('content'), function(req,res){
  var d0=req.body.param[0]; //location
  var d1=req.body.param[1]; //id
  var d2=req.body.param[2]; //doc_name
  var d2_mod = name;
  var d3=req.body.param[3]; //doc_dir
  var d4=req.body.param[4]; //doc_col
  var d5=req.body.param[5]; //doc_face
  var d6=req.body.param[6]; //num
  var d7=req.body.param[7]; //doc_type

  fb.doc_upload(d0,d1,d2_mod,d3,d4,d5,d6,d2);


  //cmd 연동
  var spawn =require('child_process').spawn;
  var ls;
  if(d7 == 'pdf'){
    console.log("pdf file");  // java, jar, name, doc_dir, doc_col, doc_face, doc_num;;;;;

  ls = spawn('java',['-jar','C:/users/John/desktop/run.jar',d2_mod,d3,d4,d5,d6]);
}else{

    console.log("jpg file (not pdf file)");
  ls = spawn('java',['-jar','C:/users/John/desktop/printer2.jar',d2_mod,d3,d4,d5,d6]);

}
  ls.stdout.on('data', function (data) {
      //  console.log('stdout: ' + data);
  });
  res.end("0");
  //완료후 파이어베이스에 완료로 추가업데이트

});

app.post('/location',upload.single('param'),function(req,res){
  console.log("trying to get location info");
  var d0= req.body.param[0];
  console.log("id = "+d0);

  fb.location(d0,function(response){
    console.log("got all the infos");
    console.log("res = "+response);
    res.end(response);
  });

})

app.post('/doc_del',upload.single('param'),function(req,res){
  console.log("delete info");
  var d0= req.body.param[0];
  var d1= req.body.param[1];
  var d2= req.body.param[2];
  console.log("? " +d0);
  console.log("!" +d1);
  fb.doc_del(d0,d1,d2);
    res.end("0");

})


app.listen(80,function(){
  console.log('connected');
})
