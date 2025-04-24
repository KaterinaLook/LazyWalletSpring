    function pressKey(value) {
        const display = document.getElementById("display");
        let current = display.textContent;

        if (current === "0") current = "";
        current += value;

        display.textContent = current;
        document.getElementById("amountInput").value = current;
    }

    function backspace() {
        let display = document.getElementById("display");
        let current = display.textContent;

        if (current.length > 1) {
            display.textContent = current.slice(0, -1);
            document.getElementById("amountInput").value = display.textContent;
        } else {
            display.textContent = "0";
            document.getElementById("amountInput").value = "0";
        }
    }

    const categorySelect = document.getElementById('categorySelect');
    const newCategoryModal = document.getElementById('newCategoryModal');
    const newCategoryName = document.getElementById('newCategoryName');
    const submitNewCategory = document.getElementById('submitNewCategory');
    const closeModalBtn = document.querySelector('.close');

    function populateCategorySelect() {
        const selectedType = document.getElementById('type').value.toUpperCase();
        categorySelect.innerHTML = '';

        fetch('/api/categories')
            .then(response => response.json())
            .then(data => {
                const filtered = data.filter(category => category.type === selectedType);
                filtered.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.name;
                    option.textContent = category.name;
                    categorySelect.appendChild(option);
                });

                const addOption = document.createElement('option');
                addOption.value = "add_new_category";
                addOption.textContent = "Add new category...";
                categorySelect.appendChild(addOption);
            })
            .catch(error => {
                console.error("Error loading categories:", error);
            });
    }

    categorySelect.addEventListener('change', function () {
        if (categorySelect.value === 'add_new_category') {
            newCategoryModal.style.display = 'block';
        }
    });

    closeModalBtn.addEventListener('click', function () {
        newCategoryModal.style.display = 'none';
    });

    window.addEventListener('click', function (event) {
        if (event.target === newCategoryModal) {
            newCategoryModal.style.display = 'none';
        }
    });

   submitNewCategory.addEventListener('click', function () {
       const newCategory = newCategoryName.value.trim();
       const selectedType = document.getElementById('type').value.toUpperCase(); // "EXPENSE" или "INCOME"

       if (!newCategory) {
           alert("Enter category!");
           return;
       }

       // Подготовим JSON объект
       const categoryData = {
           name: newCategory,
           type: selectedType
       };

       // Отправим POST-запрос
       fetch('/api/categories/add', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(categoryData)
       })
       .then(response => {
           if (response.ok) {
               return response.text();
           } else if (response.status === 409) {
               throw new Error("This category already exists");
           } else {
               throw new Error("Failed to add category");
           }
       })
       .then(message => {
           alert(message);
           newCategoryModal.style.display = 'none';
           newCategoryName.value = '';
           populateCategorySelect();
       })
       .catch(error => {
           alert(error.message);
       });
   });

    // Настройка диаграммы расходов
   let expensesChart; // глобальная переменная

   function loadExpenseChart() {
       fetch('/api/transactions/stats')
           .then(response => response.json())
           .then(data => {
               const labels = data.map(item => item.category);
               const values = data.map(item => item.total);

               if (expensesChart) {
                   expensesChart.destroy();
               }

               const ctx = document.getElementById('expensesChart').getContext('2d');
               expensesChart = new Chart(ctx, {
                   type: 'pie',
                   data: {
                       labels: labels,
                       datasets: [{
                           label: 'Expenses',
                           data: values,
                           backgroundColor: [
                               '#ff6384', '#36a2eb', '#cc65fe', '#fee165',
                               '#4bc0c0', '#9966ff', '#ff9f40', '#c9cbcf'
                           ]
                       }]
                   },
                   options: {
                       responsive: true,
                       maintainAspectRatio: false
                   }
               });
           })
           .catch(error => console.error("Error loading chart:", error));
   }

document.getElementById('transactionForm').addEventListener('submit', function (event) {
    event.preventDefault(); // отключаем обычную отправку формы

    const amount = document.getElementById('display').textContent.trim();
    const type = document.getElementById('type').value;
    const category = document.getElementById('categorySelect').value;

    if (!amount || amount === "0") {
        alert("Add amount");
        return;
    }

    if (!category || category === "add_new_category") {
        alert("Please choose a valid category");
        return;
    }

    const formData = new URLSearchParams();
    formData.append("amount", amount);
    formData.append("type", type);
    formData.append("category", category);

    fetch('/add-transaction', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
    .then(response => {
        if (response.ok) {
            // Успешно добавлено, обновим график
            loadExpenseChart();
            document.getElementById('display').textContent = "0";
            document.getElementById('amountInput').value = "0";
        } else {
            throw new Error("Error adding transaction");
        }
    })
    .catch(error => {
        console.error(error);
        alert("Failed to add transaction");
    });
});

window.addEventListener('DOMContentLoaded', () => {
    populateCategorySelect();
    loadExpenseChart(); // uploading the chart when the page loads
});
