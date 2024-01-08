import uuid 
from locust import HttpUser, task
class Website(HttpUser):
    def on_start(self):
        self.client.post(
            f"/patients/{1}",
            json={
                "id": 1,
                "name": "Test Patient",
                "email": f"{1}@gmail.com",
            }
        )

    @task
    def get_timeslots(self):
        random_uuid = uuid.uuid4()
        self.client.get(f"/offices/{random_uuid}")
    
    @task
    def create_patient(self):
        random_uuid = uuid.uuid4()
        self.client.post(
            f"/patients/{random_uuid}",
            json={
                "id": str(random_uuid),
                "name": "Test Patient",
                "email": f"{random_uuid}@gmail.com",
            }
        )
    
    @task
    def patch_patient(self):
        self.client.patch(
            f"/patients/{1}",
            json={
                "id": 1,
                "name": "New Test Patient"
            }
        )