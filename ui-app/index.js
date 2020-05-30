
function shortenUrl() {
    console.log("Submit event fired")
    $.ajax({
        url: "http://localhost:4321/shorten",
        type: "POST",
        data: JSON.stringify({longUrl: "http://google.com"}),
        success: function(data) {
            var row = document.createElement("TR");                 // Create a <li> node
            var longCol = document.createElement("TD");                 // Create a <li> node
            var shortCol = document.createElement("TD");                 // Create a <li> node
            var textnode = document.createTextNode(data["longUrl"]);         // Create a text node
            var textnode2 = document.createTextNode(data["id"]);         // Create a text node
            longCol.appendChild(textnode)
            shortCol.appendChild(textnode2)
            row.appendChild(longCol)
            row.appendChild(shortCol)

            document.getElementById("urls").appendChild(row)

        }
    });
    return false
}