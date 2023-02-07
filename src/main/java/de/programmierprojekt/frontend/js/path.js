function readPath() {
    let path = document.getElementById("path").value;
    
    if(path == "") {
        alert("You must enter a path");
    } else {
        $('*').css('cursor','wait');
        $.ajax({
            type: "PUT",
            url: "http://localhost:8000/prepare?" + path,
            success: function () {
                $('*').css('cursor','initial');
                window.location.href = 'http://localhost:8000/main/'; 
            },
        });
    }
}