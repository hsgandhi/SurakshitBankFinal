/**
 * 
 */
function disableKeys()
{
	
$(document).unbind('keydown').bind('keydown', function (e) {
	  var $target = $(e.target||e.srcElement);
	  if(e.keyCode == 8 && !$target.is('input,[contenteditable="true"],textarea'))
	  {
	    e.preventDefault();
	  }
	  else if(e.keyCode == 123 || e.keyCode == 116)
	  {
		    e.preventDefault();
	  }
	});
    $(document).bind("contextmenu",function(e){
        e.preventDefault();
    });
    
//17->Ctrl
//123-->F12
//116-->F5
}

function isNullOrEmptyString(varToBeTested)
{
	if(varToBeTested==null || varToBeTested=="")
		{
			return true;
		}
	return false;
}