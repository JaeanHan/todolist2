const selectedTypeButton = document.querySelector(".selected-type-button");
const typeSelectedBoxList = document.querySelector(".type-select-box-list");
const typeSelectedBoxListItems = typeSelectedBoxList.querySelectorAll("li");
const todoContentList = document.querySelector(".todo-content-list");
const sectionBody = document.querySelector(".section-body");
const incompleteCountNumber = document.querySelector(
  ".incomplete-count-number"
);
const modalContainer = document.querySelector(".modal-container");
const todoAddButton = document.querySelector(".todo-add-button");

let totalPage = 0;
let page = 1;
const contentCount = 20;
let listType = "all";
const KEYCODE_ENTER = 13;

load();

sectionBody.onscroll = () => {
  let checkNum =
    todoContentList.clientHeight -
    sectionBody.offsetHeight -
    sectionBody.scrollTop;

  if (checkNum < 1 && checkNum > -1 && page < totalPage + 1) {
    page++;
    load();
  }
};

selectedTypeButton.onclick = () => {
  typeSelectedBoxList.classList.toggle("visible");
};

function resetPage() {
  page = 1;
}

function removeAllClassList(elements, className) {
  for (let element of elements) {
    element.classList.remove(className);
  }
}

function setListType(selectedType) {
  listType = selectedType.toLowerCase();
}

function clearTodoContentList() {
  todoContentList.innerHTML = "";
}

for (let i = 0; i < typeSelectedBoxListItems.length; i++) {
  typeSelectedBoxListItems[i].onclick = () => {
    resetPage();

    removeAllClassList(typeSelectedBoxListItems, "type-selected");

    typeSelectedBoxListItems[i].classList.add("type-selected");

    setListType(typeSelectedBoxListItems[i].textContent);

    const selectedType = document.querySelector(".selected-type");

    selectedType.textContent = typeSelectedBoxListItems[i].textContent;

    clearTodoContentList();

    load();

    typeSelectedBoxList.classList.toggle("visible");
  };
}

//=================================================== event

function setTotalPage(totalCount) {
  totalPage =
    totalCount % contentCount === 0
      ? totalCount / contentCount
      : Math.floor(totalCount / contentCount) + 1;
}

function setIncompleteCount(incompleteCount) {
  incompleteCountNumber.textContent = incompleteCount;
}

function createList(todoList) {
  for (let content of todoList) {
    const listContent = `
              <li class="todo-content">
              <input
                type="checkbox"
                id="complete-check-${content.todoCode}"
                class="complete-check"
                ${content.todoComplete ? "checked" : ""}
              />
              <label for="complete-check-${content.todoCode}"></label>
              <div class="todo-content-text">${content.todo}</div>
              <input type="text" class="todo-content-input visible" value="${
                content.todo
              }"/>
              <input
                type="checkbox"
                id="importance-check-${content.todoCode}"
                class="importance-check"
                ${content.importance === 1 ? "checked" : ""}
              />
              <label for="importance-check-${content.todoCode}"></label>
              <div class="trash-button"><i class="fa-solid fa-trash"></i></div>
            </li>
    `;
    appendList(listContent);
  }
  addEvent();
}

function appendList(listContent) {
  todoContentList.innerHTML += listContent;
}

function addCompleteEvent(todoContent, todoCode) {
  const completeCheck = todoContent.querySelector(".complete-check");

  completeCheck.onchange = () => {
    let incompleteCount = parseInt(incompleteCountNumber.textContent);

    if (completeCheck.checked) {
      incompleteCountNumber.textContent = (incompleteCount - 1).toString();
    } else {
      incompleteCountNumber.textContent = (incompleteCount + 1).toString();
    }

    updateCheckStatus("complete", todoContent, todoCode);
  };
}

function addImportanceEvent(todoContent, todoCode) {
  const importanceCheck = todoContent.querySelector(".importance-check");

  importanceCheck.onchange = () => {
    updateCheckStatus("importance", todoContent, todoCode);
  };
}

function addDeleteEvent(todoContent, todoCode) {
  const trashButton = todoContent.querySelector(".trash-button");

  trashButton.onclick = () => {
    deleteTodo(todoContent, todoCode);
  };
}

function addContentInputEvent(todoContent, todoCode) {
  const todoContentText = todoContent.querySelector(".todo-content-text");
  const todoContentInput = todoContent.querySelector(".todo-content-input");
  let todoContentOldValue = null;

  let eventFlag = false;

  todoContentText.onclick = () => {
    todoContentOldValue = todoContentInput.value;
    todoContentText.classList.toggle("visible");
    todoContentInput.classList.toggle("visible");
    todoContentInput.focus();
    eventFlag = true;
  };

  let updateTodo = () => {
    const todoContentNewValue = todoContentInput.value;
    if (getChangeStatusOfValue(todoContentOldValue, todoContentNewValue)) {
      if (updateTodoContent(todoCode, todoContentNewValue)) {
        todoContentText.textContent = todoContentNewValue;
      }
    }
  };

  todoContentText.classList.toggle("visible");
  todoContentInput.classList.toggle("visible");

  todoContentInput.onblur = () => {
    if (getChangeStatusOfValue(todoContentOldValue, todoContentInput.value)) {
      updateTodo();
    }
  };

  todoContentInput.onkeyup = (e) => {
    if (todoContentInput.value && e.keyCode === KEYCODE_ENTER) {
      eventFlag = false;
      updateTodo();
      todoContentInput.blur();
    }
  };
}

function getChangeStatusOfValue(originValue, newValue) {
  return originValue !== newValue;
}

function substringTodoCode(todoContent) {
  const completeCheck = todoContent.querySelector(".complete-check");

  const todoCode = completeCheck.getAttribute("id");
  const tokenIndex = todoCode.lastIndexOf("-");

  return todoCode.substring(tokenIndex + 1);
}

function addEvent() {
  const todoContents = document.querySelectorAll(".todo-content");

  for (let todoContent of todoContents) {
    const todoCode = substringTodoCode(todoContent);

    addCompleteEvent(todoContent, todoCode);
    addImportanceEvent(todoContent, todoCode);
    addDeleteEvent(todoContent, todoCode);
    addContentInputEvent(todoContent, todoCode);
  }
}

function updateCheckStatus(type, todoContent, todoCode) {
  let result = updateStatus(type, todoCode);

  if (
    ((type === "complete" &&
      (listType === "complete" || listType === "incomplete")) ||
      (type === "importance" && listType === "importance")) &&
    result
  ) {
    todoContentList.removeChild(todoContent);
  }
}

function errMessage(request, status, error) {
  alert("요청 실패");
  console.log(request.status);
  console.log(request.responseText);
  console.log(error);
}

// ============================================ modal

todoAddButton.onclick = () => {
  modalContainer.classList.toggle("modal-visible");
  todoContentList.style.overflow = "hidden";
  setModalEvent();
};

function uncheckedImportance(importanceFlag) {
  importanceFlag.checked = false;
}

function setModalEvent() {
  const modalCloseButton = modalContainer.querySelector(".modal-close-button");
  const modalCommitButton = modalContainer.querySelector(".modal-commit-button");
  const modalTodoInput = modalContainer.querySelector(".modal-todo-input");
  const importanceFlag = modalContainer.querySelector(".importance-check");

  modalCommitButton.onclick = () => {
    const data = {
      importance: importanceFlag.checked,
      todo: modalTodoInput.value
    }
    addTodo(data);
    modalCloseButton.click();
  };

  modalCloseButton.onclick = () => {
    modalContainer.classList.toggle("modal-visible");
    todoContentList.style.overflow = "auto";
    uncheckedImportance(importanceFlag);
    clearModalTodoInput();
  };

  modalTodoInput.onkeyup = (e) => {
    if(e.keyCode === KEYCODE_ENTER) {
      console.log('hi')
      modalCommitButton.click();
    }
  }

  function clearModalTodoInput() {
    const modalTodoInput = modalContainer.querySelector(".modal-todo-input");
    modalTodoInput.value = "";
  }

  modalContainer.onclick = (e) => {
    if (e.target === modalContainer) modalCloseButton.click();
  };
}

//============================================= ajax

function load() {
  $.ajax({
    type: "get",
    url: `api/v1/todolist/list/${listType}`,
    data: {
      page: page,
      contentCount: contentCount,
    },
    dataType: "json",
    success: (response) => {
      const todoList = response.data;

      setTotalPage(todoList[0].totalCount);
      setIncompleteCount(todoList[0].incompleteCount);
      createList(todoList);
    },
    error: errMessage,
  });
}

function updateTodoContent(todoCode, todo) {
  let successFlag = false;
  $.ajax({
    type: "put",
    url: `api/v1/todolist/todo/${todoCode}`,
    contentType: "application/json",
    data: JSON.stringify(
      // todoContentInput.value
      { todo: todo, todoCode: todoCode }
    ),
    async: false,
    dataType: "json",
    success: (response) => {
      successFlag = response.data;
    },
    error: errMessage,
  });
  return successFlag;
}

function updateStatus(type, todoCode) {
  let result = false;

  $.ajax({
    type: "put",
    async: false,
    url: `/api/v1/todolist/todo/${type}/${todoCode}`,
    dataType: "json",
    success: (response) => {
      result = response.data;
    },
    error: errMessage,
  });
  return result;
}

function deleteTodo(todoContent, todoCode) {
  $.ajax({
    type: "delete",
    url: `api/v1/todolist/todo/${todoCode}`,
    async: false,
    dataType: "json",
    success: (response) => {
      if (response.data) {
        todoContentList.removeChild(todoContent);
      }
    },
    error: errMessage,
  });
  todoContentList.removeChild(todoContent);
}

function addTodo(data) {
  $.ajax({
    type: "post",
    url: "api/v1/todolist/todo",
    contentType: "application/json",
    data: JSON.stringify(data),
    async: false,
    dataType: "json",
    success: (response) => {
      if(response.data) {
        clearTodoContentList();
        load();
      }
    },
    error: errMessage,
  });
}
