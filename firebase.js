var firebase=require("firebase");

var config = {
   apiKey: "AIzaSyA07b5Q_lHSjgxNOdyqobwzkAXilYjxe1Q",
   authDomain: "sprint-a7504.firebaseapp.com",
   databaseURL: "https://sprint-a7504.firebaseio.com",
   storageBucket: "sprint-a7504.appspot.com",
   messagingSenderId: "851989761229"
 };
 firebase.initializeApp(config);

// As an admin, the app has access to read and write all data, regardless of Security Rules
var db = firebase.database();
var ref = db.ref("restricted_access/secret_document");
var usersRef = ref.child("users");
var locRef=ref.child("locations");

exports.login=function(id,pw,callback){
  var result;
usersRef.child(id).once("value",function(snapshot){
    if(snapshot.numChildren()==0){
      result=2;
    }else{
      if(snapshot.val().password==pw){
        result=0;
      }else{
        result=1;
      }
    }
      return callback(result);
    }
  );
}



exports.location = function(id,callback){
  locRef.once("value",function(snap){

    var a="[";
    var locations = snap.val();


    for ( var i in locations){
console.log(i);
      for(var j in locations[i]){
        //console.log(locations[i][j]);
        if (j==id){
          for(var k in locations[i][j]){
            if (a!="["){
              a+=",";
            }
            a+="{\"loc\":"+i+",\""+k+"\":"+JSON.stringify(locations[i][j][k])+"}";
          }
        //  var s=JSON.stringify(locations[i][j]);
        //  a+=s;

        }
      }
    }

    a+="]"
    //console.log(a);
  return   callback(a);


  });
}


exports.doc_upload = function(d0,d1,d2,d3,d4,d5,d6,doc_name){
  console.log("Document " + d2 + " received")
locRef.child(d0).child(d1).child(d2).set({
  dir:d3,
  col:d4,
  face:d5,
  num:d6,
  ing:"going",
  name:doc_name
});
}

exports.doc_del =function(loc, id, docname){
locRef.child(loc).child(id).child(docname).remove();
}


exports.register=function(id,nm,pw,pw_c,callback){
  var count;

  usersRef.child(id).once("value",function(snapshot){
      if(snapshot.numChildren()==0){
        if(pw===pw_c){
          //success
          usersRef.child(id).set(
            {name:nm,password:pw,point:100}
          )
        count= 0;
        }else{
         count= 2;//password not match
        }
      }else{
        count=1;//already an id
      }
      return callback(count);
  }
);

}
