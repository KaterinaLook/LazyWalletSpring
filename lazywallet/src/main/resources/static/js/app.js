// Обработчик нажатия клавиши калькулятора
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

// Настройка диаграммы
const ctx = document.getElementById('expensesChart').getContext('2d');
const chart = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ['Еда', 'Транспорт', 'Развлечения', 'Food'],
        datasets: [{
            label: 'Траты',
            data: [1200, 800, 500, 700],
            backgroundColor: ['#ff6384', '#36a2eb', '#cc65fe', '#fee165']
        }]
    },
    options: {
        responsive: true, // Делает диаграмму адаптивной
        maintainAspectRatio: false, // Разрешаем изменять пропорции диаграммы
    }
});

// Обработка добавления транзакции
function addTransaction() {
    const amount = document.getElementById('display').textContent || "0";
        const type = document.getElementById('type').value;
        const category = document.getElementById('category').value;

        alert(`Добавлена операция: ${type} на ${amount} в категории ${category}`);

        // Очистим калькулятор
        document.getElementById("display").textContent = "0";
        document.getElementById("amountInput").value = "0";
        document.getElementById('category').value = "";
}

// Получаем элементы для работы с категориями
const categorySelect = document.getElementById('categorySelect');
const newCategoryForm = document.getElementById('newCategoryForm');
const newCategoryName = document.getElementById('newCategoryName');
const submitNewCategory = document.getElementById('submitNewCategory');

// Массив для хранения категорий
let categories = [
    { name: "Еда", color: "#ff6347" },
    { name: "Развлечения", color: "#32cd32" },
    { name: "Транспорт", color: "#1e90ff" }
];

// Функция для обновления выпадающего списка категорий
function populateCategorySelect() {
    categorySelect.innerHTML = ''; // Очищаем текущие элементы

    fetch('/api/categories')
            .then(response => response.json())
            .then(data => {
                data.forEach(category => {
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

// Показываем форму для добавления новой категории при выборе "Добавить новую категорию"
categorySelect.addEventListener('change', function () {
    if (categorySelect.value === 'add_new_category') {
        newCategoryForm.style.display = 'block';
    }
});

// Обработка добавления новой категории
submitNewCategory.addEventListener('click', function () {
    const newCategory = newCategoryName.value.trim();
    if (newCategory) {
        // Генерируем уникальный цвет
        const randomColor = `#${Math.floor(Math.random() * 16777215).toString(16)}`;

        // Добавляем новую категорию в массив
        categories.push({ name: newCategory, color: randomColor });

        // Обновляем выпадающий список категорий
        populateCategorySelect();

        // Обновляем метки
        updateCategoryLabels();

        // Скрываем форму и очищаем поле ввода
        newCategoryForm.style.display = 'none';
        newCategoryName.value = '';
    } else {
        alert("Введите название категории!");
    }
});

// Инициализация выпадающего списка при загрузке страницы
populateCategorySelect();

// Функция для отображения категорий в виде меток
function updateCategoryLabels() {
    const categoryLabelsDiv = document.getElementById('categoryLabels');
    categoryLabelsDiv.innerHTML = ''; // Очищаем блок перед обновлением

    // Создаем метки для каждой категории
    categories.forEach(category => {
        const label = document.createElement('span');
        label.textContent = category.name;
        label.style.backgroundColor = category.color;
        label.style.padding = '5px 10px';
        label.style.margin = '5px';
        label.style.borderRadius = '5px';
        label.style.color = 'white';
        label.style.display = 'inline-block';

        // Добавляем метку в блок
        categoryLabelsDiv.appendChild(label);
    });
}
document.getElementById('transactionForm').addEventListener('submit', function (event) {
    const amount = document.getElementById('display').textContent || "0";
    document.getElementById('amountInput').value = amount;

   if (amount === "0" || amount.trim() === "") {
        alert("Add amount");
        event.preventDefault();
    }
});
// Вызовем эту функцию, когда нужно обновить метки (например, после добавления новой категории)
updateCategoryLabels();
