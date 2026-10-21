document.querySelectorAll(".problem-select").forEach((select) => {
    new TomSelect(select, {
        create: false,
        searchField: ["text"],
        sortField: {
            field: "$order",
            direction: "asc"
        },
        plugins: ["dropdown_input"],
        placeholder: "Select a problem..."
    });
});

const tagFilter = document.querySelector("#tag-filter");
const tagList = document.querySelector("#tag-list");

if (tagFilter && tagList) {
    tagFilter.addEventListener("input", function () {
        const query = this.value.toLowerCase();
        // console.log("tag filter input:", query);

        tagList.querySelectorAll(".tag-option").forEach(function (option) {
            const label = option.querySelector("label");
            const name = label.textContent.toLowerCase();

            if (name.includes(query)) {
                option.style.display = "";
            } else {
                option.style.display = "none";
            }
        });
    });
}

const problemFilter = document.getElementById("problem-filter");
const problemRows = document.querySelectorAll("tbody tr");

problemFilter.addEventListener("input", () => {
  const query = problemFilter.value.trim().toLowerCase();

  problemRows.forEach(row => {
    const title = row.dataset.problemTitle.toLowerCase();
    row.hidden = query !== "" && !title.includes(query);
  });
});