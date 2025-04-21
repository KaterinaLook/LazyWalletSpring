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
                console.error("Ошибка загрузки категорий:", error);
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
           alert("Введите название категории!");
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
               return response.text(); // можно заменить на response.json() если нужно
           } else if (response.status === 409) {
               throw new Error("Такая категория уже существует");
           } else {
               throw new Error("Не удалось добавить категорию");
           }
       })
       .then(message => {
           alert(message);
           newCategoryModal.style.display = 'none';
           newCategoryName.value = '';
           populateCategorySelect(); // обновим список категорий
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
                   expensesChart.destroy(); // удалим предыдущий график
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
           .catch(error => console.error("Ошибка загрузки графика:", error));
   }

document.getElementById('transactionForm').addEventListener('submit', function (event) {
    const amount = document.getElementById('display').textContent || "0";
    const category = document.getElementById('categorySelect').value;

    document.getElementById('amountInput').value = amount;

    if (amount === "0" || amount.trim() === "") {
        alert("Add amount");
        event.preventDefault();
    }

    if (!category || category === "add_new_category") {
        alert("Please choose a valid category");
        event.preventDefault();
    }
});

window.addEventListener('DOMContentLoaded', () => {
    populateCategorySelect();
    loadExpenseChart(); // 🟢 подгружаем график при загрузке страницы
});
