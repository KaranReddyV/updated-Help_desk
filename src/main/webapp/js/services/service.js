angular.module('exampleApp').factory('myfactory',function($q,$http){
    
  var f={};
  
    f.get=function(url){
        var deferred=$q.defer();
        var request={
            method:'GET',
            url:url
        };
        $http(request).success(function(resp){
         
            deferred.resolve(resp);

        })
        .error(function(resp){
            deferred.reject(resp);
        });
        return deferred.promise;
    }
    f.post=function(url,requestdata){
    	 var deferred=$q.defer();
         var request={
             method:'POST',
             data:requestdata,
             url:url,
             headers:{
            	 'Content-Type': undefined
             }
         };
         $http(request).success(function(resp){
          
             deferred.resolve(resp);

         })
         .error(function(resp){
             deferred.reject(resp);
         });
         return deferred.promise;
    }
    f.posting=function(url,requestdata){
    	var deferred=$q.defer();
    	 var request={
    			 method:"POST",
    			 data:requestdata,
    			 url:url
    	 }
    	 $http(request).success(function(resp){
    		 deferred.resolve(resp);
    	 })
    	 .error(function(resp){
    		 deferred.reject(resp); 
    	 })
         return deferred.promise;
    }
    f.put=function(url,requestdata){
   	 var deferred=$q.defer();
        var request={
            method:'PUT',
            data:requestdata,
            url:url,
   
            headers:{
           	 'Content-Type': undefined
            }
          
        };
        $http(request).success(function(resp){
         
            deferred.resolve(resp);

        })
        .error(function(resp){
            deferred.reject(resp);
        });
        return deferred.promise;
   }
    f.putdata=function(url,requestdata){
      	 var deferred=$q.defer();
           var request={
               method:'PUT',
               data:requestdata,
               url:url,
 
             
           };
           $http(request).success(function(resp){
            
               deferred.resolve(resp);

           })
           .error(function(resp){
               deferred.reject(resp);
           });
           return deferred.promise;
      }
    f.delte=function(url){
      	 var deferred=$q.defer();
           var request={
               method:'DELETE',
               url:url
             
           };
           $http(request).success(function(resp){
            
               deferred.resolve(resp);

           })
           .error(function(resp){
               deferred.reject(resp);
           });
           return deferred.promise;
      }
    return f;
});

