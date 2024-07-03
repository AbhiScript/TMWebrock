class Student
{
constructor(rollNo,name,gender)
{
this.rollNo=rollNo;
this.name=name;
this.gender=gender;
}
}

class StudentService{
add(student){
return fetch('http://localhost:8080/TMWebRock/StudentService/studentAction/add',{
method : 'POST',
headers : {
'Content-Type' : 'application/json'
},
body : JSON.stringify(student)
})
.then(response=>{
if(!response.ok)
{
throw new Error(`HTTP error! Status : ${response.status}`);
}
return response.text();
});
}

update(student){
return fetch('http://localhost:8080/TMWebRock/StudentService/studentAction/update',{
method : 'POST',
headers : {
'Content-Type' : 'application/json'
},
body : JSON.stringify(student)
});
}

delete(rollNo){
return fetch(`http://localhost:8080/TMWebRock/StudentService/studentAction/delete?id=${rollNo}`,{
method : 'POST'
});
}

getById(rollNo){
return fetch(`http://localhost:8080/TMWebRock/StudentService/studentAction/getById?id=${rollNo}`)
.then(response=>{
if(!response.ok){
throw new Error(`Http error! Status : ${response.status}`);
}
return response.json();
})
.then(data=>{
if(data.name==undefined)
{
return data;
}
else
{
return new Student(data.rollNo,data.name,data.gender);
}
})
}

getAll(){
return fetch('http://localhost:8080/TMWebRock/StudentService/studentAction/getAll')
.then(response => {
if(!response.ok) {
throw new Error(`HTTP error! Status: ${response.status}`);
}
return response.json();
})
.catch(error => {
console.error('Fetch error:', error);
throw error;
});
}

}

document.addEventListener('DOMContentLoaded',function(){

const studentService=new StudentService();

document.getElementById('addForm').addEventListener('submit',function(event){
event.preventDefault();
const name=document.getElementById("addName").value;
const gender=document.getElementById("addGender").value;
const student = new Student(null, name, gender);
console.log(student);
studentService.add(student).then(student=>{
alert("Student added");
document.getElementById('addForm').reset();
});
});

document.getElementById('updateForm').addEventListener('submit',function(event){
event.preventDefault();
const rollNo=document.getElementById('updateRollNo').value;
const name=document.getElementById('updateName').value;
const gender=document.getElementById('updateGender').value;
const student=new Student(rollNo,name,gender);
studentService.update(student).then(()=>{
alert("Student updated");
document.getElementById("updateForm").reset();
});
});

document.getElementById('getByIdForm').addEventListener('submit',function(event){
event.preventDefault();
const rollNo=document.getElementById('getByIdRollNo').value;
studentService.getById(rollNo).then(student=>{
const studentInfo=document.getElementById('studentInfo');
if(studentInfo)
{
if(student.name!=undefined)
{
studentInfo.innerText=`Roll No : ${student.rollNo}, Name : ${student.name}, Gender : ${student.gender}`;
}
else
{
studentInfo.innerText=student;
}
}
document.getElementById('getByIdForm').reset();
}).catch(error=>{
console.log('Error is getById : '+error);
const studentInfo=document.getElementById('studentInfo');
if(studentInfo){
studentInfo.innerText='Error fetching student';
}
});
});


document.getElementById('deleteForm').addEventListener('submit',function(event){
event.preventDefault();
const rollNo=document.getElementById('deleteRollNo').value;
studentService.delete(rollNo).then(()=>{
alert("student delete");
document.getElementById("delete form reset");
});
});

document.getElementById('refreshButton').addEventListener('click',function(){
studentService.getAll().then(students=>{
const tbody=document.getElementById('studentTable').getElementsByTagName('tbody')[0];
tbody.innerHTML=' ';
students.forEach(student=>{
const row=tbody.insertRow();
const cell1=row.insertCell(0);
const cell2=row.insertCell(1);
const cell3=row.insertCell(2);
cell1.innerText=student.rollNo;
cell2.innerText=student.name;

cell3.innerText=student.gender;
});
});
});
});
