fetch("logs/attacks.json")
.then(res => res.json())
.then(data => {

    let table = document.getElementById("attackTable");
    let timeline = document.getElementById("timeline");

    let riskStats = { low: 0, medium: 0, high: 0 };

    // Stats
    document.getElementById("total").innerText = data.length;
    document.getElementById("high").innerText =
        data.filter(a => a.threatLevel === "HIGH").length;

    // 🌍 MAP INIT
    let map = L.map('map').setView([20, 0], 2);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Map data © OpenStreetMap'
    }).addTo(map);

    // LOOP DATA
    data.forEach(attack => {

        // 📋 TABLE
        let row = table.insertRow();

        row.insertCell(0).innerText = attack.ip;
        row.insertCell(1).innerText = attack.country;
        row.insertCell(2).innerText = attack.city;
        row.insertCell(3).innerText = attack.org;

        let riskCell = row.insertCell(4);
        let threatCell = row.insertCell(5);

        riskCell.innerText = attack.riskScore;
        threatCell.innerText = attack.threatLevel;

        row.insertCell(6).innerText = attack.commands.join(", ");

        // 🎨 Risk colors
        if (attack.threatLevel === "HIGH") {
            riskCell.className = "high";
            riskStats.high++;
        } else if (attack.threatLevel === "MEDIUM") {
            riskCell.className = "medium";
            riskStats.medium++;
        } else {
            riskCell.className = "low";
            riskStats.low++;
        }

        // 📜 Timeline
        let li = document.createElement("li");
        li.innerText = `${attack.timestamp} → ${attack.ip} (${attack.attackType})`;
        timeline.appendChild(li);

        // 🌍 Map markers (with fallback)
        if (!attack.lat || attack.lat === 0) {
            attack.lat = 28.6139;
            attack.lon = 77.2090;
        }

        let marker = L.circleMarker([attack.lat, attack.lon], {
            radius: 8,
            color: 'red',
            fillColor: '#ff0000',
            fillOpacity: 0.8
        }).addTo(map);

        marker.bindPopup(
            `<b>${attack.ip}</b><br>
             ${attack.country}<br>
             Risk: ${attack.riskScore}<br>
             Threat: ${attack.threatLevel}`
        );
    });

    // 📊 Chart
    new Chart(document.getElementById("riskChart"), {
        type: 'bar',
        data: {
            labels: ["Low", "Medium", "High"],
            datasets: [{
                label: "Attack Levels",
                data: [riskStats.low, riskStats.medium, riskStats.high]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

});