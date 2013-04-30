
$(document).ready(function(){
    

    $(window).resize(function(){
        updateMainDivsHeight();
    });
    
    $(document).bind('pagebeforeshow', function(){
        updateMainDivsHeight();
    });
    
});

function updateMainDivsHeight(){
    docHeight = $(document).height();
    updateHeightPX = ((docHeight - 50) +"px");
    $(".divBuddiesAndGroups").css({ height: updateHeightPX });
    $(".divChatRoom").css({ height: updateHeightPX });
}