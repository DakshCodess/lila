import rollupProject from '@build/rollupProject';

export default rollupProject({
  main: {
    name: 'PlayStrategySwiss',
    input: 'src/main.ts',
    output: 'swiss',
  },
});
