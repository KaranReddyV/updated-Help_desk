
app.config(function($stateProvider, $urlRouterProvider){

    $urlRouterProvider.otherwise('/login');
    $stateProvider
    .state("/login",{
        url:"/login",
        templateUrl:"partials/Login.html",
        controller:"loginctrl"
    })
       .state("raiseticket",{
        url:"/raiseticket",
        templateUrl:"partials/Raiseticket.html",
         controller:"raisetcktctrl"

    })
     .state("raiseticket.Dashboard",{
        url:"/Dashboard",
		templateUrl:"partials/Dashboard.html",
		controller:"raisetcktctrl"
    })
     .state("raiseticket.Admindashboard",{
        url:"/Admindashboard",
		templateUrl:"partials/admindashboard.html",
		controller:"raisetcktctrl"
    })
    .state("raiseticket.raiseticketservices",{
        url:"/raiseticketservices",
        templateUrl:"partials/Raiseticketservices.html",
        controller:"raisetcktctrl"
    })
     .state("raiseticket.raiseticketpage",{
        url:"/raiseticketpage",
        templateUrl:"partials/Raiseticketpage.html",
        controller:"raisetcktctrl"
    })
     .state("regpage",{
        url:"/regpage",
		templateUrl:"partials/Registration.html",
		controller:"regctrl"

    })
     .state("forgotpwdpage",{
        url:"/forgotpwdpage",
		templateUrl:"partials/ForgotPassword.html",
		controller:"forgotpwdctrl"

    })
     .state("newpwd",{
        url:"/newpwd",
		templateUrl:"partials/Createnewpassword.html",
		controller:"newpwdctrl"

    })
    .state("raiseticket.viewtickets",{
        url:"/viewtickets",
templateUrl:"partials/Viewtickets.html",
controller:"updateticketsctrl"

    })
     .state("raiseticket.editform",{
        url:"/editform/:ticketid",
    templateUrl:"partials/Editform.html",
    controller:"editformctrl"

    })
     .state("raiseticket.admineditform",{
     url:"/admineditform/:ticketid",
    templateUrl:"partials/admineditform.html",
    controller:"admineditformctrl"

    })
     .state("raiseticket.assignedissues",{
        url:"/assignedissues",
		templateUrl:"partials/Assignedisuues.html",
		controller:"assignticketsctrl"

    })
      .state("raiseticket.alltickets",{
        url:"/alltickets",
	templateUrl:"partials/alltickets.html",
	controller:"viewticketctrl"

    })
      .state("raiseticket.feedback",{
    	url:"/feedback",
    	templateUrl:"partials/feedback.html",
    	controller:"feedbackctrl"

    })
    .state("raiseticket.ticketdetails", {
    	url:"/ticketdetails/:ticketid",
    	templateUrl:"partials/ticketdetails.html",
    	controller:"ticketdetailsctrl"
    })
});
