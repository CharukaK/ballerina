function forkWithWorkers() returns int {
    fork {
        worker forkWorker1 returns int {
            int a = 1;
            return a;
        }

        worker forkWorker2 returns int {
            int a = 2;
            return a;
        }
    }

    worker newWorker1 returns int {
        int a = 3;
        return a;
    }

    worker newWorker2 returns int {
        int a = 4;
        return a;
    }

    map<int> result = wait {forkWorker1, forkWorker2, newWorker1, newWorker2};

    return result.forkWorker1 + result.forkWorker2 + result.newWorker1 + result.newWorker2;
}