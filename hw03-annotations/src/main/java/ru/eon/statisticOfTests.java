package ru.eon;

// record StatisticOfTests(int totalTests, int totalSuccess, int totalFailure) {
//    StatisticOfTests incrementSuccess() {
//        return new StatisticOfTests(this.totalTests + 1, this.totalSuccess + 1, this.totalFailure);
//    }
//
//    StatisticOfTests incrementFailure() {
//        return new StatisticOfTests(this.totalTests + 1, this.totalSuccess, this.totalFailure + 1);
//    }
//
//    StatisticOfTests convertFromArray(int[] targetArray) {
//        return new StatisticOfTests(targetArray[0] + targetArray[1], targetArray[0], targetArray[1]);
//    }
//
//    StatisticOfTests mergeStatistics(StatisticOfTests target) {
//        return new StatisticOfTests(
//                this.totalTests + target.totalTests,
//                this.totalSuccess + target.totalSuccess,
//                this.totalFailure + target.totalFailure);
//    }
// }
