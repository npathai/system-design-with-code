
function shortenUrl() {
    console.log("Submit event fired")
    $.ajax({
        url: "http://localhost:4321/shorten",
        type: "POST",
        data: JSON.stringify({longUrl: document.getElementById("longUrl").value}),
        success: function(data) {
            var row = document.createElement("TR");                 // Create a <li> node
            var longCol = document.createElement("TD");                 // Create a <li> node
            var shortCol = document.createElement("TD");                 // Create a <li> node
            var textnode = document.createTextNode(data["longUrl"]);         // Create a text node
            var linkNode = document.createElement("A");
            linkNode.href = "http://localhost:4321/" + data["id"];
            linkNode.innerText = "http://localhost:4321/" + data["id"];
            longCol.appendChild(textnode)
            shortCol.appendChild(linkNode)
            row.appendChild(longCol)
            row.appendChild(shortCol)

            document.getElementById("urls").appendChild(row)

        }
    });
    return false
}