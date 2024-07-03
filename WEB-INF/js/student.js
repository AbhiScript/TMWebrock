class Student
{
constructor(rollNo, name, gender)
{
this.rollNo=rollNo;
this.name=name;
this.gender=gender;
}
}

class StudentAction{
add(Student) {
return fetch(`/StudentAction/add`, {
method : 'POST,
headers : {'Content-Type' : 'application/json'},
body : JSON.stringify({Student}),
})
.then(response=>{
if(!response.ok){
throw new Error (`HTTP error! Status : ${response.status}`)
}
return response.json();
})
.catch(error)=>{
console.error('Fetch error : ',error);
throw error
});
}

update(Student) {
return fetch(`/StudentAction/update`, {
method : 'POST,
headers : {'Content-Type' : 'application/json'},
body : JSON.stringify({Student}),
})
.then(response=>{
if(!response.ok){
throw new Error (`HTTP error! Status : ${response.status}`)
}
})
.catch(error)=>{
console.error('Fetch error : ',error);
throw error
});
}

getAll() {
return fetch(`/StudentAction/getAll`, {
method : 'GET,
})
.then(response=>{
if(!response.ok){
throw new Error (`HTTP error! Status : ${response.status}`)
}
return response.json();
})
.catch(error)=>{
console.error('Fetch error : ',error);
throw error
});
}

Delete() {
return fetch(`/StudentAction/delete`, {
method : 'POST,
})
.then(response=>{
if(!response.ok){
throw new Error (`HTTP error! Status : ${response.status}`)
}
})
.catch(error)=>{
console.error('Fetch error : ',error);
throw error
});
}

getById() {
return fetch(`/StudentAction/getById?id=${id}`, {
method : 'GET,
})
.then(response=>{
if(!response.ok){
throw new Error (`HTTP error! Status : ${response.status}`)
}
return response.json();
})
.catch(error)=>{
console.error('Fetch error : ',error);
throw error
});
}

}
