function getContestList(callback) {
    var server = "https://codeforces.com/api/contest.list?gym=false";
    $.getJSON(server, function (data) {
        var contests = [];

        for (var i = 0; i < data.result.length; i++) {
            var id = data.result[i].id;
            var name = data.result[i].name;
            var startTime = data.result[i].startTimeSeconds;
            var phase = data.result[i].phase;

            if (name.length > 1 && startTime > 0 && phase !== "BEFORE") {
                var item = {
                    contestId: parseInt(id),
                    name: name,
                    startTime: parseInt(startTime)
                };

                contests.push(item);
            }
        }

        contests.sort(function (a, b) {
            if (a.startTime > b.startTime) return -1;
            if (a.startTime < b.startTime) return 1;
            return 0;
        });

        callback(contests);
    });
}

function updateForm() {
    var select = document.getElementById('selectContest');
    var option = select.options[select.selectedIndex];
    var contestName = document.getElementById('contestName');
    contestName.value = option.getAttribute('contestName');
}

getContestList(function (contests) {
    var select = document.getElementById("selectContest");

    for (var i = 0; i < contests.length; i++) {
        var option = document.createElement('option');

        option.setAttribute('value', contests[i].contestId);
        option.setAttribute('contestName', contests[i].name);
        option.appendChild(document.createTextNode(contests[i].name));

        select.appendChild(option);
    }

    updateForm();
});