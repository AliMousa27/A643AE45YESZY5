import datetime
from locust import HttpUser, task


class DentistUI(HttpUser):
    def on_start(self):
        self.dentist_data = {
            "name": "John Doe",
            "dateOfBirth": "1980-01-01 16:00"
        }
        self.client.post("/api/dentists", json=self.dentist_data)

        self.office_data = {
            "name": "Test Office",
            "address": "123 Main St",
            "longitude": 22,
            "latitude": 22
        }
        self.client.post("/api/offices", json=self.office_data)

    @task()
    def get_all_dentists(self):
        self.client.get("/api/dentists")

    @task()
    def update_dentist(self):
        dentist_id = 1
        update_data = {"name": "Jane Doe"}
        self.client.patch(f"/api/dentists/{dentist_id}", json=update_data)
    
    @task
    def create_dentist(self):
        self.client.post("/api/dentists", json=self.dentist_data)

    @task
    def create_office(self):
        self.client.post("/api/offices", json=self.office_data)

    @task
    def get_all_offices(self):
        self.client.get("/api/offices")
