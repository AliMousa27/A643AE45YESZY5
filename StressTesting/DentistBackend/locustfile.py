import datetime
import random
from locust import HttpUser, task

class Dentist(HttpUser):
    def on_start(self):
        self.id=None
        dentist_data = {"name": "John Doe", "dateOfBirth": "1980-01-01 16:00"}  
        self.client.post("/api/dentists", json=dentist_data)
    @task()
    def get_all_dentists(self):
        self.client.get("/api/dentists")

    @task()
    def update_dentist(self):
        dentist_id = 1
        update_data = {"name": "Jane Doe"}  
        self.client.patch(f"/api/dentists/{dentist_id}", json=update_data)
    
    @task
    def create_office(self):
        self.client.post(
            "/api/offices",
            json={
                "name": "Test Office",
                "address": "123 Main St",
                "longitude": 22,
                "latitude": 22
            }
        )
    @task
    def get_all_offices(self):
        self.client.get("/api/offices")

    @task(10)
    def create_timeslot(self):
        days_in_future = random.randint(1, 20000)
        date = datetime.date.today() + datetime.timedelta(days=random.randint(1, days_in_future))
        date = date.__str__() + " 16:00"
        self.client.post(
            "/api/timeslots",
            json={
                "dentistId": 1,
                "officeId": 1,   
                "dateAndTime": date  
            }
        )
    
