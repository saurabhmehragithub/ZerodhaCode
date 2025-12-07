document.addEventListener('DOMContentLoaded', function() {
    const loginBtn = document.getElementById('loginBtn');
    if (loginBtn) {
        loginBtn.addEventListener('click', function() {
            fetch('/api/login-url')
                .then(res => res.json())
                .then(data => showAuthPrompt(data.loginUrl));
        });
    }
});


// Show authentication UI
        function showAuthPrompt(loginUrl) {
           document.getElementById('app').innerHTML = `
            <h2>Authenticate with Zerodha</h2>
            <p>1. <a href="${loginUrl}" target="_blank">Click here to login to Zerodha</a></p>
            <p>2. After login, copy the <b>request_token</b> from the browser URL and paste it below:</p>
            <input type="text" id="requestTokenInput" placeholder="Paste request_token here" style="width:300px;">
            <button onclick="sendRequestToken()">Authenticate</button>
            <div id="authStatus"></div>
            <hr>
            <div id="mainContent" style="display:none;"></div>
            `;
         }         

        function sendRequestToken() {
            const token = document.getElementById('requestTokenInput').value;
            fetch('/api/authenticate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ requestToken: token })
            })
            .then(res => res.json())
            .then(ok => {
                if (ok) {
                    document.getElementById('authStatus').innerHTML = "<span style='color:green'>Authenticated! Loading data...</span>";
                    setTimeout(loadMainContentFunc, 1000);
                } else {
                    document.getElementById('authStatus').innerHTML = "<span style='color:red'>Authentication failed. Try again.</span>";
                }
            });
        }

         function loadMainContentFunc() {
     
            document.getElementById('appHoldingsPositions').innerHTML = `
                    <h1>Zerodha Holdings</h1>
                    <table id="holdingsTable">
                        <thead>
                            <tr>
                                <th>Symbol</th>
                                <th>Quantity</th>
                                <th>Last Price</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                    <h1>Zerodha Positions</h1>
                    <table id="positionsTable">
                        <thead>
                            <tr>
                                <th>Symbol</th>
                                <th>Quantity</th>
                                <th>Last Price</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                `;

        // Fetch and display holdings
        fetch('/api/holdings')
            .then(response => response.json())
            .then(data => {
                const tbody = document.querySelector('#holdingsTable tbody');
                data.forEach(h => {
                    const row = `<tr>
                        <td>${h.tradingSymbol}</td>
                        <td>${h.quantity}</td>
                        <td>${h.lastPrice}</td>
                    </tr>`;
                    tbody.insertAdjacentHTML('beforeend', row);
                    console.log(h);
                });
            })
            .catch(err => {
                document.querySelector('#holdingsTable tbody').innerHTML = `<tr><td colspan="3">Error loading holdings static folder</td></tr>`;
            });

        // Fetch and display positions
        fetch('/api/positions')
            .then(response => response.json())
            .then(data => {
                const tbody = document.querySelector('#positionsTable tbody');
                data.forEach(p => {
                    const row = `<tr>
                        <td>${p.tradingSymbol}</td>
                        <td>${p.netQuantity}</td>
                        <td>${p.lastPrice}</td>
                    </tr>`;
                    tbody.insertAdjacentHTML('beforeend', row);
                });
            })
            .catch(err => {
                document.querySelector('#positionsTable tbody').innerHTML = `<tr><td colspan="3">Error loading positions</td></tr>`;
            });
        } //Load main content after authentication end
