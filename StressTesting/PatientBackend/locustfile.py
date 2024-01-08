import random
from locust import HttpUser, task
class Patient(HttpUser):
    
    def on_start(self):
        id = random.randint(1, 100000000).__str__()
        self.client.post(
            f"/patients/{id}",
            json={
                "id": self.id,
                "name": "Test Patient",
                "email": f"{self.id}@gmail.com",
            }
        )
    @task
    def get_timeslots(self):
        self.client.get(
            f"/offices/{1}"
        )