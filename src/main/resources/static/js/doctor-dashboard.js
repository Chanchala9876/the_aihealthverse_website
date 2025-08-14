//doctor-dashboard.js
document.addEventListener('DOMContentLoaded', function() {
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    var doctorEmail = document.getElementById('doctorEmail').value;

    stompClient.connect({}, function(frame) {
        // Subscribe to personal consultation updates
        stompClient.subscribe('/user/' + doctorEmail + '/queue/consultation-updates', function(message) {
            var consultation = JSON.parse(message.body);
            updateDashboard(consultation);
        });
    });

    function updateDashboard(consultation) {
        // Update counts
        updateStats();
        
        // Move consultation card to appropriate section based on status
        var consultationCard = document.querySelector(`[data-consultation-id="${consultation.id}"]`);
        if (consultationCard) {
            var targetSection;
            switch(consultation.status) {
                case 'PENDING':
                    targetSection = document.querySelector('#pending .card-body');
                    break;
                case 'IN_PROGRESS':
                    targetSection = document.querySelector('#active .card-body');
                    break;
                case 'COMPLETED':
                    targetSection = document.querySelector('#completed .card-body');
                    break;
            }
            if (targetSection) {
                consultationCard.remove();
                targetSection.appendChild(consultationCard);
            }
        } else {
            // If card doesn't exist, reload the page to show new consultation
            location.reload();
        }
    }

    function updateStats() {
        fetch('/api/doctor/consultation-stats')
            .then(response => response.json())
            .then(stats => {
                document.querySelector('#pending-count').textContent = stats.pending;
                document.querySelector('#active-count').textContent = stats.active;
                document.querySelector('#completed-count').textContent = stats.completed;
                document.querySelector('#total-count').textContent = stats.total;
            });
    }
});
